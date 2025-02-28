package finan.repository;

import finan.model.ContaCorrente;
import finan.model.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    /**
     * Busca lançamentos por período
     */
    List<Lancamento> findByContaAndDataMovimentoBetweenOrderByDataMovimento(
            ContaCorrente conta, LocalDate dataInicio, LocalDate dataFim);

    /**
     * Calcula o saldo da conta até uma data específica
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN l.tipo = 'CREDITO' THEN l.valor ELSE -l.valor END), 0) " +
            "FROM Lancamento l WHERE l.conta = ?1 AND l.dataMovimento <= ?2")
    BigDecimal calcularSaldoAteData(ContaCorrente conta, LocalDate data);

    /**
     * Verifica se um débito causaria saldo negativo em alguma data futura
     */
    @Query("SELECT MIN(COALESCE(SUM(CASE WHEN l.tipo = 'CREDITO' THEN l.valor ELSE -l.valor END), 0))" +
            " FROM Lancamento l WHERE l.conta = ?1 AND l.dataMovimento >= ?2 GROUP BY l.dataMovimento")
    BigDecimal verificarSaldoFuturo(ContaCorrente conta, LocalDate dataInicio);

}
