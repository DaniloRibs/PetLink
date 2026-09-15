package br.fai.faitec.petlink2026.ports_and_adapters.port.service.annoucement;

import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementModel;
import br.fai.faitec.petlink2026.dto.annoucement.ReadAnnouncementDto;

import java.util.List;

public interface ReadAnnoucementService {

    List<AnnoucementModel> showAllAnnoucementsByUserId(final int userId);

    ReadAnnouncementDto getAnnouncementById(int id);

    List<ReadAnnouncementDto> getAnnouncements();

    List<ReadAnnouncementDto> getAnnouncementsByUserId(int userId);

}
