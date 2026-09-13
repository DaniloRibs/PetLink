package br.fai.faitec.petlink2026.ports_and_adapters.port.dao.annoucement;

import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.crud.CreateDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.crud.DeleteDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.crud.ReadDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud.CreateService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud.DeleteService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.crud.FindService;

public interface AnnoucementDao extends CreateDao<AnnoucementModel>, DeleteDao, ReadDao<AnnoucementModel> {


}
