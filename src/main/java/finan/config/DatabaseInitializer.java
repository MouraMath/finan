package finan.config;

import finan.model.ContaCorrente;
import finan.model.TipoAtivo;
import finan.model.Ativo;
import finan.repository.AtivoRepository;
import finan.repository.ContaCorrenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/**
 * Classe responsável por inicializar o banco de dados com dados iniciais
 * Substitui a necessidade de um arquivo data.sql que não é bem suportado no IntelliJ Community
 */
@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initDatabase(AtivoRepository ativoRepository,
                                          ContaCorrenteRepository contaCorrenteRepository) {
        return args -> {
            // Inicializa a conta corrente
            if (contaCorrenteRepository.count() == 0) {
                ContaCorrente conta = new ContaCorrente();
                conta.setDescricao("Conta Principal");
                contaCorrenteRepository.save(conta);
            }

            // Inicializa alguns ativos de exemplo
            if (ativoRepository.count() == 0) {
                Ativo ativo1 = new Ativo();
                ativo1.setNome("PETR4");
                ativo1.setTipo(TipoAtivo.RV);
                ativo1.setDataEmissao(LocalDate.now().minusYears(1));
                ativo1.setDataVencimento(LocalDate.now().plusYears(5));
                ativoRepository.save(ativo1);

                Ativo ativo2 = new Ativo();
                ativo2.setNome("TESOURO");
                ativo2.setTipo(TipoAtivo.RF);
                ativo2.setDataEmissao(LocalDate.now().minusMonths(6));
                ativo2.setDataVencimento(LocalDate.now().plusYears(2));
                ativoRepository.save(ativo2);
            }
        };
    }
}
