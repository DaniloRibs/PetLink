package br.fai.lds.projetolds2026.ports_and_adapters.port.dao.user;

import br.fai.lds.projetolds2026.domain.user.UserModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.crud.CrudDao;

public interface UserDao extends CrudDao<UserModel>, ReadByEmailDao,
        UpdatePasswordDao {


}
