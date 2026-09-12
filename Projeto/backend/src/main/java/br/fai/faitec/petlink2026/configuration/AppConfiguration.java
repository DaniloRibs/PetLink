package br.fai.faitec.petlink2026.configuration;

import br.fai.faitec.petlink2026.ports_and_adapters.adapter.dao.user.UserFakeDaoAdapter;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public UserDao getUserFakeDao() {
        return new UserFakeDaoAdapter();
    }

//    @Bean
//    public UserDao getUserPostgresDao(final Connection connection) {
//        return new UserPostgresDaoAdapter(connection);
//    }


}
