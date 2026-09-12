package br.fai.faitec.petlink2026.ports_and_adapters.port.service.user;

import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud.CrudService;

public interface UserService extends CrudService<UserModel>, FindByEmailService,
        UpdatePasswordService, ReadPetsService {

}
