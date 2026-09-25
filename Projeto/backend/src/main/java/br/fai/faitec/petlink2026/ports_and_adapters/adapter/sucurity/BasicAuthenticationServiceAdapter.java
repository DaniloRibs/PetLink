package br.fai.faitec.petlink2026.ports_and_adapters.adapter.sucurity;


import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.security.AuthenticationService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BasicAuthenticationServiceAdapter implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public BasicAuthenticationServiceAdapter(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserModel authenticate(String email, String password) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        UserModel userModel = userService.findByEmail(email);
        if (userModel == null) {
            return null;
        }
//        if (userModel.getPassword().equals(password)) {
//            return userModel;
//        }

        if (passwordEncoder.matches(password, userModel.getPassword())) {
            return userModel;
        }

        return null;
    }
}
