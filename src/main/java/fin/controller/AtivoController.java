package fin.controller;

import fin.model.*;
import fin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ativos")
@RequiredArgsConstructor
public class AtivoController {
    private final AtivoService ativoService;
    private final ContaCorrenteService contaCorrenteService;

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody Ativo ativo) {
        ativoService.cadastrarAtivo(ativo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{nome}")
    public ResponseEntity<Ativo> buscar(@PathVariable String nome) {
        return ResponseEntity.ok(ativoService.buscarAtivo(nome));
    }

    @PutMapping("/{nome}")
    public ResponseEntity<Void> atualizar(@PathVariable String nome, @RequestBody Ativo ativo) {
        ativoService.atualizarAtivo(nome, ativo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{nome}")
    public ResponseEntity<Void> deletar(@PathVariable String nome) {
        ativoService.deletarAtivo(nome);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comprar")
    public ResponseEntity<Void> comprar(@RequestBody Movimentacao movimentacao) {
        ativoService.comprar(movimentacao, contaCorrenteService);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/vender")
    public ResponseEntity<Void> vender(@RequestBody Movimentacao movimentacao) {
        ativoService.vender(movimentacao, contaCorrenteService);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posicao")
    public ResponseEntity<List<PosicaoAtivo>> getPosicao() {
        return ResponseEntity.ok(ativoService.getPosicao());
    }
}
