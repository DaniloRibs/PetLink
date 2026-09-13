package br.fai.faitec.petlink2026.ports_and_adapters.port.service.annoucement;

import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud.CreateService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud.DeleteService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud.FindService;

public interface AnnoucementService extends CreateService<AnnoucementModel>, DeleteService, FindService<AnnoucementModel> {


}
