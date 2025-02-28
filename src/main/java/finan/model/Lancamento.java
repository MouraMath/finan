package finan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.*;
import java.time.LocalDate;

/**
 * Representa um lançamento financeiro na conta corrente
 * Adicionei a data do movimento e o tipo (crédito ou débito) para facilitar consultas
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private ContaCorrente conta;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Data de movimento é obrigatória")
    private LocalDate dataMovimento;

    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    public enum TipoLancamento {
        CREDITO, DEBITO
    }
}
