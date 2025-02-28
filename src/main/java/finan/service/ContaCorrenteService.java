package finan.service;

import finan.exception.ResourceNotFoundException;
import finan.model.ContaCorrente;
import finan.model.Lancamento;
import finan.repository.ContaCorrenteRepository;
import finan.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaCorrenteService {

    private final ContaCorrenteRepository contaCorrenteRepository;
    private final LancamentoRepository lancamentoRepository;

    /**
     * Busca uma conta corrente pelo ID
     */
    public ContaCorrente buscarPorId(Long id) {
        return contaCorrenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta corrente não encontrada: " + id));
    }

    /**
     * Busca todas as contas correntes
     */
    public List<ContaCorrente> buscarTodas() {
        return contaCorrenteRepository.findAll();
    }

    /**
     * Calcula o saldo da conta até uma data específica
     */
    public BigDecimal calcularSaldo(ContaCorrente conta, LocalDate data) {
        return lancamentoRepository.calcularSaldoAteData(conta, data);
    }

    /**
     * Registra um lançamento de crédito
     */
    @Transactional
    public Lancamento creditar(ContaCorrente conta, Lancamento lancamento) {
        validarLancamento(lancamento);
        lancamento.setConta(conta);
        lancamento.setTipo(Lancamento.TipoLancamento.CREDITO);
        return lancamentoRepository.save(lancamento);
    }

    /**
     * Registra um lançamento de débito
     */
    @Transactional
    public Lancamento debitar(ContaCorrente conta, Lancamento lancamento) {
        validarLancamento(lancamento);

        // Verifica se há saldo suficiente na data do lançamento
        BigDecimal saldoAtual = lancamentoRepository.calcularSaldoAteData(conta, lancamento.getDataMovimento());
        if (saldoAtual.compareTo(lancamento.getValor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na data do lançamento");
        }

        // Verifica se o débito não causará saldo negativo em datas futuras
        BigDecimal saldoFuturo = lancamentoRepository.verificarSaldoFuturo(conta, lancamento.getDataMovimento());
        if (saldoFuturo != null && saldoFuturo.compareTo(lancamento.getValor()) < 0) {
            throw new IllegalArgumentException("Este débito causaria saldo negativo em datas futuras");
        }

        lancamento.setConta(conta);
        lancamento.setTipo(Lancamento.TipoLancamento.DEBITO);
        return lancamentoRepository.save(lancamento);
    }

    /**
     * Busca lançamentos por período
     */
    public List<Lancamento> buscarLancamentosPorPeriodo(ContaCorrente conta, LocalDate dataInicio, LocalDate dataFim) {
        return lancamentoRepository.findByContaAndDataMovimentoBetweenOrderByDataMovimento(conta, dataInicio, dataFim);
    }

    /**
     * Valida os dados do lançamento
     */
    private void validarLancamento(Lancamento lancamento) {
        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do lançamento deve ser positivo");
        }

        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do lançamento é obrigatória");
        }

        if (lancamento.getDataMovimento() == null) {
            throw new IllegalArgumentException("Data do movimento é obrigatória");
        }
    }
}
