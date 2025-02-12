package fin.controller;

import fin.model.Lancamento;
import fin.service.ContaCorrenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/conta-corrente")
@RequiredArgsConstructor
public class ContaCorrenteController {
    private final ContaCorrenteService service;

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> getSaldo() {
        return ResponseEntity.ok(service.getSaldo());
    }

    @PostMapping("/credito")
    public ResponseEntity<Void> creditar(@RequestBody Lancamento lancamento) {
        service.creditar(lancamento);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/debito")
    public ResponseEntity<Void> debitar(@RequestBody Lancamento lancamento) {
        service.debitar(lancamento);
        return ResponseEntity.ok().build();
    }
}
