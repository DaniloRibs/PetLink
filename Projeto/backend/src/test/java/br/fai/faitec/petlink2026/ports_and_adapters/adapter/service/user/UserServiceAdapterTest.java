package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.user;

import br.fai.faitec.petlink2026.domain.user.PersonModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.PersonDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceAdapterTest {

    @InjectMocks
    private UserServiceAdapter userService;

    @Test
    public void shouldRejectEmptyPassword() {
        assertTrue(userService.isPasswordInvalid(""));
    }

    @Test
    public void shouldRejectTooShortPassword() {
        assertTrue(userService.isPasswordInvalid("a"));
    }

    @Test
    public void shouldRejectNegativeId() {
        assertTrue(userService.isIdInvalid(-1));
    }


}
