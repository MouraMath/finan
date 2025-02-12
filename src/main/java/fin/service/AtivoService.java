package fin.service;

import fin.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtivoService {
    private final Map<String, Ativo> ativos = new HashMap<>();
    private final Map<String, List<Movimentacao>> movimentacoes = new HashMap<>();

    public void cadastrarAtivo(Ativo ativo) {
        validarAtivo(ativo);
        ativos.put(ativo.getNome(), ativo);
    }

    public Ativo buscarAtivo(String nome) {
        return ativos.get(nome);
    }

    public void atualizarAtivo(String nome, Ativo ativo) {
        validarAtivo(ativo);
        if (!ativos.containsKey(nome)) {
            throw new IllegalArgumentException("Ativo não encontrado");
        }
        ativos.put(nome, ativo);
    }

    public void deletarAtivo(String nome) {
        if (!ativos.containsKey(nome)) {
            throw new IllegalArgumentException("Ativo não encontrado");
        }
        ativos.remove(nome);
    }

    private void validarAtivo(Ativo ativo) {
        if (ativo.getNome() == null || ativo.getPrecoMercado() == null || ativo.getTipo() == null) {
            throw new IllegalArgumentException("Todos os campos são obrigatórios");
        }
    }

    public void comprar(Movimentacao movimentacao, ContaCorrenteService contaCorrenteService) {
        Ativo ativo = ativos.get(movimentacao.getAtivo());
        if (ativo == null) {
            throw new IllegalArgumentException("Ativo não encontrado");
        }

        contaCorrenteService.debitar(new Lancamento(
                movimentacao.getValor(),
                "Compra de " + ativo.getNome(),
                movimentacao.getData()
        ));

        movimentacoes.computeIfAbsent(ativo.getNome(), k -> new ArrayList<>()).add(movimentacao);
    }

    public void vender(Movimentacao movimentacao, ContaCorrenteService contaCorrenteService) {
        Ativo ativo = ativos.get(movimentacao.getAtivo());
        if (ativo == null) {
            throw new IllegalArgumentException("Ativo não encontrado");
        }

        BigDecimal quantidadeTotal = getQuantidadeTotal(ativo.getNome());
        if (quantidadeTotal.compareTo(movimentacao.getQuantidade()) < 0) {
            throw new IllegalArgumentException("Quantidade insuficiente para venda");
        }

        contaCorrenteService.creditar(new Lancamento(
                movimentacao.getValor(),
                "Venda de " + ativo.getNome(),
                movimentacao.getData()
        ));

        movimentacoes.get(ativo.getNome()).add(movimentacao);
    }

    public List<PosicaoAtivo> getPosicao() {
        List<PosicaoAtivo> posicoes = new ArrayList<>();

        for (Ativo ativo : ativos.values()) {
            BigDecimal quantidadeTotal = getQuantidadeTotal(ativo.getNome());
            BigDecimal valorMercadoTotal = quantidadeTotal.multiply(ativo.getPrecoMercado())
                    .setScale(2, RoundingMode.DOWN);
            BigDecimal precoMedioCompra = calcularPrecoMedioCompra(ativo.getNome());
            BigDecimal rendimento = precoMedioCompra.compareTo(BigDecimal.ZERO) > 0 ?
                    ativo.getPrecoMercado().divide(precoMedioCompra, 8, RoundingMode.DOWN) :
                    BigDecimal.ZERO;
            BigDecimal lucro = calcularLucro(ativo.getNome());

            posicoes.add(new PosicaoAtivo(
                    ativo.getNome(),
                    ativo.getTipo(),
                    quantidadeTotal,
                    valorMercadoTotal,
                    rendimento,
                    lucro
            ));
        }

        return posicoes;
    }

    private BigDecimal getQuantidadeTotal(String ativo) {
        return movimentacoes.getOrDefault(ativo, new ArrayList<>()).stream()
                .map(m -> m.getQuantidade().multiply(
                        m.getValor().compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.ONE : BigDecimal.valueOf(-1)
                ))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularPrecoMedioCompra(String ativo) {
        List<Movimentacao> compras = movimentacoes.getOrDefault(ativo, new ArrayList<>()).stream()
                .filter(m -> m.getValor().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());


        if (compras.isEmpty()) return BigDecimal.ZERO;

        BigDecimal totalValor = compras.stream()
                .map(Movimentacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalQuantidade = compras.stream()
                .map(Movimentacao::getQuantidade)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalQuantidade.compareTo(BigDecimal.ZERO) > 0 ?
                totalValor.divide(totalQuantidade, 8, RoundingMode.DOWN) :
                BigDecimal.ZERO;
    }

    private BigDecimal calcularLucro(String ativo) {
        return movimentacoes.getOrDefault(ativo, new ArrayList<>()).stream()
                .map(Movimentacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::subtract);
    }
}
