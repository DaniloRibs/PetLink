package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.announcement;

import br.fai.faitec.petlink2026.domain.announcement.AnnouncementModel;
import br.fai.faitec.petlink2026.domain.user.AccountType;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.dto.announcement.ReadAnnouncementDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.announcement.AnnouncementDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.announcement.AnnouncementService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.announcement.ReadAnnouncementService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementServiceAdapter implements AnnouncementService {

    @Autowired
    private AnnouncementDao announcementDao;
    @Autowired
    private UserService userService;

    @Override
    public int create(AnnouncementModel announcement) {
        if (announcement == null) {
            return 0;
        }

        if (announcement.getTitle() == null || announcement.getTitle().isEmpty()) {
            return 0;
        }

        if (announcement.getDescription() == null || announcement.getDescription().isEmpty()) {
            return 0;
        }

        if (announcement.getContact() == null || announcement.getContact().isEmpty()) {
            return 0;
        }

        UserModel creator = userService.findById(announcement.getIdCreator());

        if (creator == null) {
            return 0;
        }

        if (creator.getAccountType() != AccountType.ENTERPRISE) {
            return 0;
        }

        return announcementDao.add(announcement);
    }

    @Override
    public void delete(int id) {

        if (isIdInvalid(id)) {
            return;
        }

        AnnouncementModel announcement = announcementDao.readyById(id);

        if (announcement == null) {
            return;
        }

        announcementDao.remove(id);

    }

    @Override
    public AnnouncementModel findById(int id) {
        if (isIdInvalid(id)) {
            return null;
        }
        return announcementDao.readyById(id);
    }

    boolean isIdInvalid(int id) {
        return id < 0;
    }

    @Override
    public List<AnnouncementModel> findAll() {
        return announcementDao.readAll();
    }

    @Override
    public List<AnnouncementModel> showAllAnnouncementsByUserId(int userId) {
        if (isIdInvalid(userId)) {
            return List.of();
        }

        UserModel userModel = userService.findById(userId);
        if (userModel == null) {
            return List.of();
        }

        List<AnnouncementModel> announcementModels = new ArrayList<>();

        for (AnnouncementModel announcementModel : findAll()) {
            if (announcementModel.getIdCreator() == userId) {
                announcementModels.add(announcementModel);
            }
        }


        return announcementModels;
    }

    @Override
    public ReadAnnouncementDto getAnnouncementById(int id) {
        AnnouncementModel announcementModel = findById(id);

        if (announcementModel == null) {
            return null;
        }

        UserModel userModel = userService.findById(announcementModel.getIdCreator());

        if (userModel == null) {
            return null;
        }

        return new ReadAnnouncementDto(
                announcementModel.getId(),
                announcementModel.getTitle(),
                announcementModel.getDescription(),
                announcementModel.getEventDate(),
                announcementModel.getLocation(),
                userModel.getEmail(),
                userModel.getFullname(),
                announcementModel.getAnnouncementType(),
                announcementModel.getContact()
        );

    }

    @Override
    public List<ReadAnnouncementDto> getAnnouncements() {
        List<ReadAnnouncementDto> readAnnouncementDtos = new ArrayList<>();

        for (AnnouncementModel announcementModel : findAll()) {
            ReadAnnouncementDto readAnnouncementDto = getAnnouncementById(announcementModel.getId());
            if (readAnnouncementDto != null) {
                readAnnouncementDtos.add(readAnnouncementDto);
            }
        }
        return readAnnouncementDtos;

    }

    @Override
    public List<ReadAnnouncementDto> getAnnouncementsByUserId(int userId) {
        List<ReadAnnouncementDto> readAnnouncementDtos = new ArrayList<>();
        for (AnnouncementModel announcementModel : showAllAnnouncementsByUserId(userId)) {
            ReadAnnouncementDto readAnnouncementDto = getAnnouncementById(announcementModel.getId());
            if (readAnnouncementDto != null) {
                readAnnouncementDtos.add(readAnnouncementDto);
            }
        }
        return readAnnouncementDtos;
    }

    @Override
    public boolean update(int id, AnnouncementModel announcementModel) {
        AnnouncementModel dataToUpdate = findById(id);

        if (dataToUpdate == null) {
            return false;
        }

        if (announcementModel.getTitle() == null || announcementModel.getTitle().isEmpty()) {
            return false;
        }

        if (announcementModel.getDescription() == null || announcementModel.getDescription().isEmpty()) {
            return false;
        }

        dataToUpdate.setTitle(announcementModel.getTitle());
        dataToUpdate.setDescription(announcementModel.getDescription());
        dataToUpdate.setEventDate(announcementModel.getEventDate());
        dataToUpdate.setLocation(announcementModel.getLocation());
        dataToUpdate.setAnnouncementType(announcementModel.getAnnouncementType());

        if (announcementModel.getContact() != null && !announcementModel.getContact().isEmpty()) {
            dataToUpdate.setContact(announcementModel.getContact());
        }

        announcementDao.updateInformation(id, dataToUpdate);
        return true;
    }

}