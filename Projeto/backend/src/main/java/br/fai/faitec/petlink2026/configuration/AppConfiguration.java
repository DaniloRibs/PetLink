package br.fai.faitec.petlink2026.configuration;

import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.pet.PetPostgresDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.user.UserFakeDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.user.UserPostgresDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.vaccine.VaccinePostgresDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.pet.PetDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.vaccine.VaccineDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;

@Configuration
public class AppConfiguration {

//    @Bean
//    public UserDao getUserFakeDao() {
//        return new UserFakeDaoAdapter();
//    }

//    @Bean
//    public PetDao getPetFakeDao() {
//        return new PetFakeDaoAdapter();
//    }


    @Bean
    public UserDao getUserPostgresDao(final Connection connection) {
        return new UserPostgresDaoAdapter(connection);
    }

    @Bean
    public PetDao getPetPostgresDao(final Connection connection) {
        return new PetPostgresDaoAdapter(connection);
    }

    @Bean
    public VaccineDao getVaccinePostgresDao(final Connection connection) {
        return new VaccinePostgresDaoAdapter(connection);
    }


}
