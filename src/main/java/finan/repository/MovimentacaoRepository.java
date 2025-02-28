package finan.repository;

import finan.model.Ativo;
import finan.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    /**
     * Busca movimentações por período
     */
    List<Movimentacao> findByAtivoAndDataMovimentoBetweenOrderByDataMovimento(
            Ativo ativo, LocalDate dataInicio, LocalDate dataFim);

    /**
     * Calcula a quantidade total de um ativo até uma data específica
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN m.tipo = 'COMPRA' THEN m.quantidade ELSE -m.quantidade END), 0) " +
            "FROM Movimentacao m WHERE m.ativo = ?1 AND m.dataMovimento <= ?2")
    BigDecimal calcularQuantidadeAteData(Ativo ativo, LocalDate data);

    /**
     * Calcula o preço médio de compra de um ativo até uma data específica
     */
    @Query("SELECT COALESCE(SUM(m.valor) / NULLIF(SUM(m.quantidade), 0), 0) " +
            "FROM Movimentacao m WHERE m.ativo = ?1 AND m.tipo = 'COMPRA' AND m.dataMovimento <= ?2")
    BigDecimal calcularPrecoMedioCompraAteData(Ativo ativo, LocalDate data);

}
