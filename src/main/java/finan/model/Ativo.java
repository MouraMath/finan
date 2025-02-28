package finan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Representa um ativo financeiro no sistema
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ativo {
    @Id
    @NotBlank(message = "Nome do ativo é obrigatório")
    private String nome;

    @NotNull(message = "Tipo do ativo é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoAtivo tipo;

    @NotNull(message = "Data de emissão é obrigatória")
    private LocalDate dataEmissao;

    @NotNull(message = "Data de vencimento é obrigatória")
    private LocalDate dataVencimento;

    /**
     * Verifica se a data está dentro do período válido do ativo
     */
    public boolean isDataValida(LocalDate data) {
        return !data.isBefore(dataEmissao) && data.isBefore(dataVencimento);
    }

    /**
     * Verifica se é um dia útil (segunda a sexta)
     */
    public boolean isDiaUtil(LocalDate data) {
        return data.getDayOfWeek().getValue() >= 1 && data.getDayOfWeek().getValue() <= 5;
    }
}
