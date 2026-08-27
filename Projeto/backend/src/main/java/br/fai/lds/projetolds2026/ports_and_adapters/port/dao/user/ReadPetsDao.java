package br.fai.lds.projetolds2026.ports_and_adapters.port.dao.user;

import br.fai.lds.projetolds2026.domain.pet.PetModel;

import java.util.List;

public interface ReadPetsDao {

    PetModel findByIndex(final int idOwner, final int indexPet);

    List<PetModel> showALl(final int idOwner);

}
