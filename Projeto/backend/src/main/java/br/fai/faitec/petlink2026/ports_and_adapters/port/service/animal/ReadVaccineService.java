package br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;

import java.util.List;

public interface ReadVaccineService {
    VaccineModel findVaccineByAnimalId(final int idPet, final int idVaccine);

    List<VaccineModel> showAllVaccineByAnimalId(final int idPet);
}

