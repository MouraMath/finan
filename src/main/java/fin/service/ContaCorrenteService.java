package fin.service;

import fin.model.ContaCorrente;
import fin.model.Lancamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ContaCorrenteService {
    private final ContaCorrente contaCorrente = new ContaCorrente();

    public BigDecimal getSaldo() {
        return contaCorrente.getSaldo();
    }

    public void creditar(Lancamento lancamento) {
        if (lancamento.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        contaCorrente.setSaldo(contaCorrente.getSaldo().add(lancamento.getValor()));
    }

    public void debitar(Lancamento lancamento) {
        if (lancamento.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        if (contaCorrente.getSaldo().compareTo(lancamento.getValor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        contaCorrente.setSaldo(contaCorrente.getSaldo().subtract(lancamento.getValor()));
    }
}
