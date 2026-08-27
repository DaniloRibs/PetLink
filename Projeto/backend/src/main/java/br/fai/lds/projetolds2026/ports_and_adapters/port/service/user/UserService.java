package br.fai.lds.projetolds2026.ports_and_adapters.port.service.user;

import br.fai.lds.projetolds2026.domain.user.UserModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.crud.*;

public interface UserService extends CrudService<UserModel>, FindByEmailService,
        UpdatePasswordService, ReadPetsService {

}
