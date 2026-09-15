package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.annoucement;

import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementModel;
import br.fai.faitec.petlink2026.domain.pet.PetModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.dto.annoucement.ReadAnnouncementDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.annoucement.AnnoucementDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.annoucement.AnnoucementService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.annoucement.ReadAnnoucementService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnnoucementServiceAdapter implements AnnoucementService {

    @Autowired
    private AnnoucementDao annoucementDao;
    @Autowired
    private UserService userService;

    @Override
    public int create(AnnoucementModel annoucement) {
        if (annoucement == null) {
            return 0;
        }

        if (annoucement.getTitle() == null || annoucement.getTitle().isEmpty()) {
            return 0;
        }

        if (annoucement.getDescription() == null || annoucement.getDescription().isEmpty()) {
            return 0;
        }

        UserModel creator = userService.findById(annoucement.getIdCreator());

        if (creator == null) {
            return 0;
        }

        if (creator.getAccountType() != creator.getAccountType().ENTERPRISE) {
            return 0;
        }

        annoucement.setPublicationDate(new Date(System.currentTimeMillis()));

        return annoucementDao.add(annoucement);
    }

    @Override
    public void delete(int id) {

        if (isIdInvalid(id)) {
            return;
        }

        AnnoucementModel annoucement = annoucementDao.readyById(id);

        if (annoucement == null) {
            return;
        }

        annoucementDao.remove(id);

    }

    @Override
    public AnnoucementModel findById(int id) {
        if (isIdInvalid(id)) {
            return null;
        }
        return annoucementDao.readyById(id);
    }

    boolean isIdInvalid(int id) {
        return id < 0;
    }

    @Override
    public List<AnnoucementModel> findAll() {
        return annoucementDao.readAll();
    }

    @Override
    public List<AnnoucementModel> showAllAnnoucementsByUserId(int userId) {
        if (isIdInvalid(userId)) {
            return List.of();
        }

        UserModel userModel = userService.findById(userId);
        if (userModel == null) {
            return List.of();
        }

        List<AnnoucementModel> annoucementModels = new ArrayList<>();

        for (AnnoucementModel annoucementModel : findAll()) {
            if (annoucementModel.getIdCreator() == userId) {
                annoucementModels.add(annoucementModel);
            }
        }


        return annoucementModels;
    }

    @Override
    public ReadAnnouncementDto getAnnouncementById(int id) {
        AnnoucementModel annoucementModel = findById(id);
        UserModel userModel = userService.findById(annoucementModel.getIdCreator());


        return new ReadAnnouncementDto(annoucementModel.getTitle(),
                annoucementModel.getDescription(),
                annoucementModel.getPublicationDate(),
                annoucementModel.getEventDate(),
                annoucementModel.getLocation(),
                userModel.getEmail(),
                userModel.getFullname(),
                annoucementModel.getAnnoucementType()
        );

    }

    @Override
    public List<ReadAnnouncementDto> getAnnouncements() {
        List<ReadAnnouncementDto> readAnnouncementDtos = new ArrayList<>();

        for (AnnoucementModel annoucementModel : findAll()) {
            readAnnouncementDtos.add(getAnnouncementById(annoucementModel.getId()));

        }
        return readAnnouncementDtos;

    }

    @Override
    public List<ReadAnnouncementDto> getAnnouncementsByUserId(int userId) {
        List<ReadAnnouncementDto> readAnnouncementDtos = new ArrayList<>();
        for (AnnoucementModel annoucementModel : showAllAnnoucementsByUserId(userId)) {
            readAnnouncementDtos.add(getAnnouncementById(annoucementModel.getId()));

        }
        return readAnnouncementDtos;
    }

}
