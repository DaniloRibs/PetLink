package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.annoucement;

import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementModel;
import br.fai.faitec.petlink2026.domain.user.UserModel;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.annoucement.AnnoucementDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.dao.user.UserDao;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.annoucement.AnnoucementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class AnnoucementServiceAdapter implements AnnoucementService {

    @Autowired
    private AnnoucementDao annoucementDao;
    @Autowired
    private UserDao userDao;

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

        UserModel creator = userDao.readyById(annoucement.getIdCreator());

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
}
