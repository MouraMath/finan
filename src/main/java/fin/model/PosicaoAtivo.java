package fin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosicaoAtivo {
    private String nome;
    private TipoAtivo tipo;
    private BigDecimal quantidadeTotal;
    private BigDecimal valorMercadoTotal;
    private BigDecimal rendimento;
    private BigDecimal lucro;
}
