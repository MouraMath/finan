package finan.repository;

import finan.model.Ativo;
import finan.model.ValorMercadoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ValorMercadoAtivoRepository extends JpaRepository<ValorMercadoAtivo, Long> {
    /**
     * Busca o valor de mercado mais recente para um ativo até uma data específica
     */
    @Query("SELECT v FROM ValorMercadoAtivo v WHERE v.ativo = ?1 AND v.data <= ?2 ORDER BY v.data DESC")
    Optional<ValorMercadoAtivo> findMostRecentByAtivoAndDataLessThanEqual(Ativo ativo, LocalDate data);

    /**
     * Verifica se existe um valor de mercado para um ativo em uma data específica
     */
    boolean existsByAtivoAndData(Ativo ativo, LocalDate data);
}
