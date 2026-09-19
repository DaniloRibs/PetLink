package br.fai.faitec.petlink2026.ports_and_adapters.port.service.user;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.animal.PetModel;

import java.util.List;

public interface ReadPetsService {

    PetModel findPetByOwnerId(final int idOwner, final int idPet);

    List<PetModel> showAllPetsByOwnerId(final int idOwner);

    FarmAnimalModel findAnimalByOwnerId(final int idOwner, final int idPet);

    List<FarmAnimalModel> showAllAnimalByOwnerId(final int idOwner);

}
