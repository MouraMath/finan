package finan.service;

import finan.exception.ResourceNotFoundException;
import finan.model.Ativo;
import finan.repository.AtivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AtivoService {

    private final AtivoRepository ativoRepository;

    /**
     * Busca todos os ativos
     */
    public List<Ativo> buscarTodos() {
        return ativoRepository.findAll();
    }

    /**
     * Busca um ativo pelo nome
     */
    public Ativo buscarPorNome(String nome) {
        return ativoRepository.findById(nome)
                .orElseThrow(() -> new ResourceNotFoundException("Ativo não encontrado: " + nome));
    }

    /**
     * Cadastra um novo ativo
     */
    @Transactional
    public Ativo cadastrar(Ativo ativo) {
        validarAtivo(ativo);
        return ativoRepository.save(ativo);
    }

    /**
     * Atualiza um ativo existente
     */
    @Transactional
    public Ativo atualizar(String nome, Ativo ativo) {
        if (!ativoRepository.existsById(nome)) {
            throw new ResourceNotFoundException("Ativo não encontrado: " + nome);
        }
        validarAtivo(ativo);
        ativo.setNome(nome); // Garante que o nome não seja alterado
        return ativoRepository.save(ativo);
    }

    /**
     * Remove um ativo
     */
    @Transactional
    public void remover(String nome) {
        if (!ativoRepository.existsById(nome)) {
            throw new ResourceNotFoundException("Ativo não encontrado: " + nome);
        }
        ativoRepository.deleteById(nome);
    }

    /**
     * Valida os dados do ativo
     */
    private void validarAtivo(Ativo ativo) {
        if (ativo.getNome() == null || ativo.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do ativo é obrigatório");
        }

        if (ativo.getTipo() == null) {
            throw new IllegalArgumentException("Tipo do ativo é obrigatório");
        }

        if (ativo.getDataEmissao() == null) {
            throw new IllegalArgumentException("Data de emissão é obrigatória");
        }

        if (ativo.getDataVencimento() == null) {
            throw new IllegalArgumentException("Data de vencimento é obrigatória");
        }

        if (ativo.getDataVencimento().isBefore(ativo.getDataEmissao())) {
            throw new IllegalArgumentException("Data de vencimento deve ser posterior à data de emissão");
        }
    }

    /**
     * Verifica se uma data é válida para movimentação do ativo
     */
    public void validarDataMovimentacao(Ativo ativo, LocalDate data) {
        if (data.isBefore(ativo.getDataEmissao()) || !data.isBefore(ativo.getDataVencimento())) {
            throw new IllegalArgumentException(
                    "Data de movimentação deve estar entre a data de emissão e a data de vencimento");
        }

        if (data.getDayOfWeek().getValue() > 5) {
            throw new IllegalArgumentException("Movimentações só podem ocorrer de segunda a sexta-feira");
        }
    }
}
