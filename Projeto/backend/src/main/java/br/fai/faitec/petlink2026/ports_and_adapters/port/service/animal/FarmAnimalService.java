package br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud.CrudService;

public interface FarmAnimalService extends CrudService<FarmAnimalModel>, UpdateOwnerService, ReadVaccineService {

}
