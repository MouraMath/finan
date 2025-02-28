package finan.controller;

import finan.model.Movimentacao;
import finan.service.MovimentacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoController {
    private final MovimentacaoService movimentacaoService;

    @PostMapping("/comprar")
    public ResponseEntity<Movimentacao> comprar(
            @RequestParam String ativo,
            @RequestParam BigDecimal quantidade,
            @RequestParam BigDecimal valor,
            @RequestParam LocalDate data,
            @RequestParam Long contaId) {

        return ResponseEntity.ok(movimentacaoService.comprar(ativo, quantidade, valor, data, contaId));
    }

    @PostMapping("/vender")
    public ResponseEntity<Movimentacao> vender(
            @RequestParam String ativo,
            @RequestParam BigDecimal quantidade,
            @RequestParam BigDecimal valor,
            @RequestParam LocalDate data,
            @RequestParam Long contaId) {

        return ResponseEntity.ok(movimentacaoService.vender(ativo, quantidade, valor, data, contaId));
    }

    @GetMapping
    public ResponseEntity<List<Movimentacao>> listar(
            @RequestParam String ativo,
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim) {

        return ResponseEntity.ok(movimentacaoService.buscarPorPeriodo(ativo, dataInicio, dataFim));
    }
}
