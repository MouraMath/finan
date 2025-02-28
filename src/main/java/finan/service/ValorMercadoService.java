package finan.service;

import finan.exception.ResourceNotFoundException;
import finan.model.Ativo;
import finan.model.ValorMercadoAtivo;
import finan.repository.ValorMercadoAtivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValorMercadoService {

    private final ValorMercadoAtivoRepository valorMercadoRepository;
    private final AtivoService ativoService;

    /**
     * Registra um valor de mercado para um ativo em uma data específica
     */
    @Transactional
    public ValorMercadoAtivo registrarValorMercado(String nomeAtivo, LocalDate data, BigDecimal valor) {
        Ativo ativo = ativoService.buscarPorNome(nomeAtivo);

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de mercado deve ser positivo");
        }

        if (valorMercadoRepository.existsByAtivoAndData(ativo, data)) {
            throw new IllegalArgumentException("Já existe um valor de mercado para este ativo nesta data");
        }

        ValorMercadoAtivo valorMercado = new ValorMercadoAtivo();
        valorMercado.setAtivo(ativo);
        valorMercado.setData(data);
        valorMercado.setValor(valor);

        return valorMercadoRepository.save(valorMercado);
    }

    /**
     * Remove um valor de mercado
     */
    @Transactional
    public void removerValorMercado(Long id) {
        if (!valorMercadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Valor de mercado não encontrado: " + id);
        }
        valorMercadoRepository.deleteById(id);
    }

    /**
     * Busca o valor de mercado mais recente para um ativo até uma data específica
     */
    public BigDecimal buscarValorMercado(Ativo ativo, LocalDate data) {
        Optional<ValorMercadoAtivo> valorMercado = valorMercadoRepository
                .findMostRecentByAtivoAndDataLessThanEqual(ativo, data);

        return valorMercado.map(ValorMercadoAtivo::getValor)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Não há valor de mercado registrado para o ativo " + ativo.getNome() + " até a data " + data));
    }
}
