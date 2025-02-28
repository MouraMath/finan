package finan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa uma movimentação de compra ou venda de ativos
 * Adicionei a data do movimento e o tipo (compra ou venda) para facilitar consultas
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ativo_nome", nullable = false)
    private Ativo ativo;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private BigDecimal quantidade;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "Data do movimento é obrigatória")
    private LocalDate dataMovimento;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    public enum TipoMovimentacao {
        COMPRA, VENDA
    }
}
