package br.fai.faitec.petlink2026.configuration;

import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.adoption.AdoptionPostgresDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.animal.FarmAnimalPostgresDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.announcement.AnnouncementPostgresDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.animal.PetPostgresDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.user.UserPostgresDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.vaccine.VaccinePostgresDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.sucurity.BasicAuthenticationServiceAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.adapter.sucurity.JwtAuthenticationServiceAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.adoption.AdoptionDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.animal.FarmAnimalDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.announcement.AnnouncementDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.animal.PetDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.vaccine.VaccineDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.security.AuthenticationService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.util.Arrays;

@Configuration
public class AppConfiguration {

    private final Environment environment;

    public AppConfiguration(Environment environment) {
        this.environment = environment;
        System.out.println("-------------------------");
        System.out.println("Active profile: " + Arrays.toString(environment.getActiveProfiles()));
        System.out.println("-------------------------");
    }

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

    @Bean
    public AnnouncementDao getAnnouncementPostgresDao(final Connection connection) {
        return new AnnouncementPostgresDaoAdapter(connection);
    }

    @Bean
    public AdoptionDao getAdoptionPostgresDao(final Connection connection) {
        return new AdoptionPostgresDaoAdapter(connection);
    }

    @Bean
    public FarmAnimalDao getFarmAnimalPostgresDao(final Connection connection) {
        return new FarmAnimalPostgresDaoAdapter(connection);
    }


    @Profile("basic")
    @Bean
    public AuthenticationService basicAuthenticationService(final UserService userService) {
        return new BasicAuthenticationServiceAdapter(userService);
    }

    @Profile("jwt")
    @Bean
    public AuthenticationService jwtAuthenticationService() {
        return new JwtAuthenticationServiceAdapter();
    }

}
