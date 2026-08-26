package br.fai.lds.projetolds2026.ports_and_adapters.port.service.user;

import br.fai.lds.projetolds2026.domain.UserModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.crud.*;

import java.util.List;

public interface UserService extends CrudService<UserModel>,FindByEmailService, UpdatePasswordService {

}
