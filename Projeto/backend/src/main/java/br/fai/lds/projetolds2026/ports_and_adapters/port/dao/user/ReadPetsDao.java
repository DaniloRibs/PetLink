package br.fai.lds.projetolds2026.ports_and_adapters.port.service.user;

import br.fai.lds.projetolds2026.domain.pet.PetModel;

import java.util.List;

public interface ReadPetsService {

    PetModel findByIndex(final int indexPet);

    List<PetModel> showALl(final int idOwner);

}
