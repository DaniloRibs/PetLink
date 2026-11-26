package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.security.jwt;

import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.security.AuthenticationService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JwtAuthenticationServiceAdapter implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    public JwtAuthenticationServiceAdapter(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public UserModel authenticate(String email, String password) {
        System.out.println("autenticacao com jwt");

        final UserModel userModel = userService.findByEmail(email);
        if (userModel == null) {
            throw new UsernameNotFoundException("Usuario nao encontrado");
        }

        if (!passwordEncoder.matches(password, userModel.getPassword())) {
            throw new BadCredentialsException("Credenciais invalidas");
        }

        return userModel;
    }


}
