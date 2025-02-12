package fin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ativo {
    private String nome;
    private BigDecimal precoMercado;
    private TipoAtivo tipo;
}