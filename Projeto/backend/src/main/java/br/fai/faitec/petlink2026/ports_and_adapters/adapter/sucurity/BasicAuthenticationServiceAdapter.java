package br.fai.faitec.petlink2026.ports_and_adapters.adapter.sucurity;


import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.security.AuthenticationService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;

public class BasicAuthenticationServiceAdapter implements AuthenticationService {

    private final UserService userService;

    public BasicAuthenticationServiceAdapter(UserService userService) {
        this.userService = userService;
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
        if (userModel.getPassword().equals(password)) {
            return userModel;
        }
        return null;
    }
}
