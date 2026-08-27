package br.fai.lds.projetolds2026.ports_and_adapters.port.service.user;

import br.fai.lds.projetolds2026.domain.pet.PetModel;

import java.util.List;

public interface ReadPetsService {

    PetModel findPetByOwnerId(final int idOwner, final int idPet);

    List<PetModel> showALlPetsByOwnerId(final int idOwner);

}
