package finan.service;

import finan.exception.ResourceNotFoundException;
import finan.model.*;
import finan.repository.MovimentacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final AtivoService ativoService;
    private final ContaCorrenteService contaCorrenteService;
    private final ValorMercadoService valorMercadoService;

    /**
     * Valida os dados da movimentação
     */
    private void validarMovimentacao(BigDecimal quantidade, BigDecimal valor) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
    }

    /**
     * Registra uma compra de ativo
     */
    @Transactional
    public Movimentacao comprar(String nomeAtivo, BigDecimal quantidade, BigDecimal valor,
                                LocalDate dataMovimento, Long contaId) {
        Ativo ativo = ativoService.buscarPorNome(nomeAtivo);
        ContaCorrente conta = contaCorrenteService.buscarPorId(contaId);

        // Valida a data da movimentação
        ativoService.validarDataMovimentacao(ativo, dataMovimento);

        // Valida os dados da movimentação
        validarMovimentacao(quantidade, valor);

        // Cria o lançamento de débito na conta corrente
        Lancamento lancamento = new Lancamento();
        lancamento.setValor(valor);
        lancamento.setDescricao("Compra de " + quantidade + " " + ativo.getNome());
        lancamento.setDataMovimento(dataMovimento);
        contaCorrenteService.debitar(conta, lancamento);

        // Registra a movimentação
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setAtivo(ativo);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setValor(valor);
        movimentacao.setDataMovimento(dataMovimento);
        movimentacao.setTipo(Movimentacao.TipoMovimentacao.COMPRA);

        return movimentacaoRepository.save(movimentacao);
    }

    /**
     * Registra uma venda de ativo
     */
    @Transactional
    public Movimentacao vender(String nomeAtivo, BigDecimal quantidade, BigDecimal valor,
                               LocalDate dataMovimento, Long contaId) {
        Ativo ativo = ativoService.buscarPorNome(nomeAtivo);
        ContaCorrente conta = contaCorrenteService.buscarPorId(contaId);

        // Valida a data da movimentação
        ativoService.validarDataMovimentacao(ativo, dataMovimento);

        // Valida os dados da movimentação
        validarMovimentacao(quantidade, valor);

        // Verifica se há quantidade suficiente para venda
        BigDecimal quantidadeDisponivel = movimentacaoRepository.calcularQuantidadeAteData(ativo, dataMovimento);
        if (quantidadeDisponivel.compareTo(quantidade) < 0) {
            throw new IllegalArgumentException("Quantidade insuficiente para venda. Disponível: " +
                    quantidadeDisponivel + ", Solicitado: " + quantidade);
        }

        // Cria o lançamento de crédito na conta corrente
        Lancamento lancamento = new Lancamento();
        lancamento.setValor(valor);
        lancamento.setDescricao("Venda de " + quantidade + " " + ativo.getNome());
        lancamento.setDataMovimento(dataMovimento);
        contaCorrenteService.creditar(conta, lancamento);

        // Registra a movimentação
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setAtivo(ativo);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setValor(valor);
        movimentacao.setDataMovimento(dataMovimento);
        movimentacao.setTipo(Movimentacao.TipoMovimentacao.VENDA);

        return movimentacaoRepository.save(movimentacao);
    }

    /**
     * Busca movimentações por período
     */
    public List<Movimentacao> buscarPorPeriodo(String nomeAtivo, LocalDate dataInicio, LocalDate dataFim) {
        Ativo ativo = ativoService.buscarPorNome(nomeAtivo);
        return movimentacaoRepository.findByAtivoAndDataMovimentoBetweenOrderByDataMovimento(
                ativo, dataInicio, dataFim);
    }

    /**
     * Calcula a posição dos ativos em uma data específica
     */
    public List<PosicaoAtivo> calcularPosicao(LocalDate data) {
        List<Ativo> ativos = ativoService.buscarTodos();
        List<PosicaoAtivo> posicoes = new ArrayList<>();

        for (Ativo ativo : ativos) {
            BigDecimal quantidade = movimentacaoRepository.calcularQuantidadeAteData(ativo, data);

            // Se não há quantidade, não incluímos na posição
            if (quantidade.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            BigDecimal valorMercado;
            try {
                valorMercado = valorMercadoService.buscarValorMercado(ativo, data);
            } catch (ResourceNotFoundException e) {
                // Se não há valor de mercado, pulamos este ativo
                continue;
            }

            BigDecimal valorTotal = quantidade.multiply(valorMercado)
                    .setScale(2, RoundingMode.DOWN);

            BigDecimal precoMedio = movimentacaoRepository.calcularPrecoMedioCompraAteData(ativo, data);

            BigDecimal rendimento = BigDecimal.ZERO;
            if (precoMedio.compareTo(BigDecimal.ZERO) > 0) {
                rendimento = valorMercado.divide(precoMedio, 8, RoundingMode.DOWN);
            }

            PosicaoAtivo posicao = new PosicaoAtivo(
                    ativo.getNome(),
                    ativo.getTipo(),
                    data,
                    quantidade,
                    valorMercado,
                    valorTotal,
                    precoMedio,
                    rendimento
            );

            posicoes.add(posicao);
        }

        return posicoes;
    }
}
