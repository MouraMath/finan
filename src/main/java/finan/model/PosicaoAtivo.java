package finan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa a posição consolidada de um ativo na carteira em uma data
 * Esta é uma classe DTO (Data Transfer Object) usada apenas para retorno de dados
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosicaoAtivo {
    private String nome;
    private TipoAtivo tipo;
    private LocalDate data;
    private BigDecimal quantidadeTotal;
    private BigDecimal valorMercado;
    private BigDecimal valorTotal;
    private BigDecimal precoMedio;
    private BigDecimal rendimento; // percentual do rendimento
}
