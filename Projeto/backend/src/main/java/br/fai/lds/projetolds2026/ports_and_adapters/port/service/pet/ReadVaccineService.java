package br.fai.lds.projetolds2026.ports_and_adapters.port.service.pet;

import br.fai.lds.projetolds2026.domain.vaccine.RecordVaccineModel;
import br.fai.lds.projetolds2026.domain.vaccine.VaccineModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.vaccine.VaccineService;

import java.util.List;

public interface ReadVaccineService {

    VaccineModel findVaccineById (final int idPet, final int idVaccine);

    List<RecordVaccineModel> showAllVaccinePetById(final int idPet);

}
