package br.fai.faitec.petlink2026.ports_and_adapters.adapter.sucurity;


import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.security.AuthenticationService;

public class JwtAuthenticationServiceAdapter implements AuthenticationService {
    @Override
    public UserModel authenticate(String email, String password) {

        System.out.println("authenticação com jwt");
        return null;
    }
}
