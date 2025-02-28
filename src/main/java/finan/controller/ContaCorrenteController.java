package finan.controller;

import finan.model.ContaCorrente;
import finan.model.Lancamento;
import finan.service.ContaCorrenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/conta-corrente")
@RequiredArgsConstructor
public class ContaCorrenteController {
    private final ContaCorrenteService service;

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> getSaldo(@RequestParam Long contaId, @RequestParam LocalDate data) {
        ContaCorrente conta = service.buscarPorId(contaId);
        return ResponseEntity.ok(service.calcularSaldo(conta, data));
    }

    @PostMapping("/credito")
    public ResponseEntity<Lancamento> creditar(@RequestParam Long contaId, @RequestBody Lancamento lancamento) {
        ContaCorrente conta = service.buscarPorId(contaId);
        return ResponseEntity.ok(service.creditar(conta, lancamento));
    }

    @PostMapping("/debito")
    public ResponseEntity<Lancamento> debitar(@RequestParam Long contaId, @RequestBody Lancamento lancamento) {
        ContaCorrente conta = service.buscarPorId(contaId);
        return ResponseEntity.ok(service.debitar(conta, lancamento));
    }

    @GetMapping("/lancamentos")
    public ResponseEntity<List<Lancamento>> buscarLancamentos(
            @RequestParam Long contaId,
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim) {
        ContaCorrente conta = service.buscarPorId(contaId);
        return ResponseEntity.ok(service.buscarLancamentosPorPeriodo(conta, dataInicio, dataFim));
    }
}
