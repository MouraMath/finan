package finan.controller;

import finan.model.Ativo;
import finan.model.PosicaoAtivo;
import finan.service.AtivoService;
import finan.service.MovimentacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ativos")
@RequiredArgsConstructor
public class AtivoController {
    private final AtivoService ativoService;
    private final MovimentacaoService movimentacaoService;

    @GetMapping
    public ResponseEntity<List<Ativo>> listarTodos() {
        return ResponseEntity.ok(ativoService.buscarTodos());
    }

    @PostMapping
    public ResponseEntity<Ativo> cadastrar(@RequestBody Ativo ativo) {
        return ResponseEntity.ok(ativoService.cadastrar(ativo));
    }

    @GetMapping("/{nome}")
    public ResponseEntity<Ativo> buscar(@PathVariable String nome) {
        return ResponseEntity.ok(ativoService.buscarPorNome(nome));
    }

    @PutMapping("/{nome}")
    public ResponseEntity<Ativo> atualizar(@PathVariable String nome, @RequestBody Ativo ativo) {
        return ResponseEntity.ok(ativoService.atualizar(nome, ativo));
    }

    @DeleteMapping("/{nome}")
    public ResponseEntity<Void> deletar(@PathVariable String nome) {
        ativoService.remover(nome);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posicao")
    public ResponseEntity<List<PosicaoAtivo>> getPosicao(@RequestParam LocalDate data) {
        return ResponseEntity.ok(movimentacaoService.calcularPosicao(data));
    }
}
