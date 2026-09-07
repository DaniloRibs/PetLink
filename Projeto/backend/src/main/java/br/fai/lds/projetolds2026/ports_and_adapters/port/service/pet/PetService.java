package br.fai.lds.projetolds2026.ports_and_adapters.port.service.pet;

import br.fai.lds.projetolds2026.domain.pet.PetModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.crud.CrudService;

public interface PetService extends CrudService<PetModel>, UpdateOwnerService {

}
