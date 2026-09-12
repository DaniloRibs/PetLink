package br.fai.faitec.petlink2026.ports_and_adapters.port.service.pet;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;

import java.util.List;

public interface ReadVaccineService {
    VaccineModel findVaccineByPetId(final int idPet, final int idVaccine);

    List<VaccineModel> showAllVaccineByPetId(final int idPet);
}

