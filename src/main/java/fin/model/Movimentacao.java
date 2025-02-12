package fin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {
    private String ativo;
    private BigDecimal quantidade;
    private BigDecimal valor;
    private LocalDateTime data;
}
