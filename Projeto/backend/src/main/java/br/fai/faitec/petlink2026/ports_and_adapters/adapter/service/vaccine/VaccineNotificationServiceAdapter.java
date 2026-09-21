package br.fai.faitec.petlink2026.ports_and_adapters.adapter.service.vaccine;

import br.fai.faitec.petlink2026.domain.animal.AnimalModel;
import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.animal.PetModel;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineAlertModel;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineAlertStatus;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineType;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.user.UserService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.vaccine.VaccineNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VaccineNotificationServiceAdapter implements VaccineNotificationService {

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");

    private static final int DAYS_BEFORE_EXPIRATION = 15;

    @Autowired
    private UserService userService;

    @Override
    public List<VaccineAlertModel> findAlertsByOwnerId(final int ownerId) {
        final LocalDate today = LocalDate.now(ZONE);
        final List<VaccineAlertModel> alerts = new ArrayList<>();

        for (final PetModel pet : userService.showAllPetsByOwnerId(ownerId)) {
            alerts.addAll(findAlerts(pet, false, today));
        }

        for (final FarmAnimalModel farmAnimal : userService.showAllAnimalByOwnerId(ownerId)) {
            alerts.addAll(findAlerts(farmAnimal, true, today));
        }

        alerts.sort(Comparator
                .comparing(VaccineAlertModel::getStatus)
                .thenComparing(Comparator.comparingLong(VaccineAlertModel::getDaysOverdue).reversed())
                .thenComparingLong(VaccineAlertModel::getDaysUntilExpiration));

        return alerts;
    }

    private List<VaccineAlertModel> findAlerts(final AnimalModel animal, final boolean farmAnimal, final LocalDate today) {
        final List<VaccineAlertModel> alerts = new ArrayList<>();

        for (final VaccineModel latestDose : latestDosePerVaccine(animal).values()) {
            if (latestDose.getExpirationDate() == null) {
                continue;
            }

            final LocalDate expiration = latestDose.getExpirationDate().toLocalDate();
            final long daysUntilExpiration = ChronoUnit.DAYS.between(today, expiration);

            final VaccineAlertModel alert = new VaccineAlertModel();

            if (daysUntilExpiration < 0) {
                alert.setStatus(VaccineAlertStatus.EXPIRED);
                alert.setDaysOverdue(-daysUntilExpiration);
            } else if (daysUntilExpiration <= DAYS_BEFORE_EXPIRATION) {
                alert.setStatus(VaccineAlertStatus.EXPIRING);
                alert.setDaysUntilExpiration(daysUntilExpiration);
            } else {
                continue;
            }

            alert.setAnimalId(animal.getId());
            alert.setAnimalName(displayName(animal));
            alert.setSpecies(animal.getSpecies());
            alert.setFarmAnimal(farmAnimal);
            alert.setVaccineId(latestDose.getId());
            alert.setVaccineName(VaccineType.displayNameOf(latestDose.getName()));
            alert.setApplicationDate(latestDose.getApplicationDate());
            alert.setExpirationDate(latestDose.getExpirationDate());

            alerts.add(alert);
        }

        return alerts;
    }

    private Map<String, VaccineModel> latestDosePerVaccine(final AnimalModel animal) {
        final Map<String, VaccineModel> latestByVaccine = new LinkedHashMap<>();

        if (animal.getVaccines() == null) {
            return latestByVaccine;
        }

        for (final VaccineModel dose : animal.getVaccines()) {
            if (dose.getApplicationDate() == null) {
                continue;
            }

            final String vaccineKey = VaccineType.groupKey(dose.getName());
            final VaccineModel current = latestByVaccine.get(vaccineKey);

            if (current == null || isMoreRecent(dose, current)) {
                latestByVaccine.put(vaccineKey, dose);
            }
        }

        return latestByVaccine;
    }

    // Vence a aplicacao mais recente; no mesmo dia, vale a que foi cadastrada por ultimo.
    private boolean isMoreRecent(final VaccineModel candidate, final VaccineModel current) {
        final int byApplicationDate = candidate.getApplicationDate().compareTo(current.getApplicationDate());

        if (byApplicationDate != 0) {
            return byApplicationDate > 0;
        }

        return candidate.getId() > current.getId();
    }

    private String displayName(final AnimalModel animal) {
        if (animal.getName() != null && !animal.getName().isBlank()) {
            return animal.getName().trim();
        }

        if (animal instanceof FarmAnimalModel) {
            final String identify = ((FarmAnimalModel) animal).getIdentify();

            if (identify != null && !identify.isBlank()) {
                return identify.trim();
            }
        }

        return "Animal sem nome";
    }
}
