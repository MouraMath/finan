package finan.controller;

import finan.model.ValorMercadoAtivo;
import finan.service.ValorMercadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/valor-mercado")
@RequiredArgsConstructor
public class ValorMercadoController {
    private final ValorMercadoService valorMercadoService;

    @PostMapping
    public ResponseEntity<ValorMercadoAtivo> registrar(
            @RequestParam String ativo,
            @RequestParam LocalDate data,
            @RequestParam BigDecimal valor) {
        return ResponseEntity.ok(valorMercadoService.registrarValorMercado(ativo, data, valor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        valorMercadoService.removerValorMercado(id);
        return ResponseEntity.ok().build();
    }
}
