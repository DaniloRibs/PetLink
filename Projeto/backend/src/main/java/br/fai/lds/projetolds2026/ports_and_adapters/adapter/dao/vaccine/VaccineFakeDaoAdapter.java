package br.fai.lds.projetolds2026.ports_and_adapters.adapter.dao.vaccine;

import br.fai.lds.projetolds2026.domain.vaccine.VaccineModel;
import br.fai.lds.projetolds2026.ports_and_adapters.port.dao.vaccine.VaccineDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VaccineFakeDaoAdapter implements VaccineDao {
    @Override
    public int create(VaccineModel entity) {
        return 0;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public VaccineModel findById(int id) {
        return null;
    }

    @Override
    public List<VaccineModel> findAll() {
        return List.of();
    }

    @Override
    public boolean update(int id, VaccineModel entity) {
        return false;
    }
}
