package br.fai.faitec.petlink2026.ports_and_adapters.port.service.announcement;

import br.fai.faitec.petlink2026.domain.announcement.AnnouncementModel;
import br.fai.faitec.petlink2026.dto.announcement.ReadAnnouncementDto;

import java.util.List;

public interface ReadAnnouncementService {

    List<AnnouncementModel> showAllAnnouncementsByUserId(final int userId);

    ReadAnnouncementDto getAnnouncementById(int id);

    List<ReadAnnouncementDto> getAnnouncements();

    List<ReadAnnouncementDto> getAnnouncementsByUserId(int userId);

}
