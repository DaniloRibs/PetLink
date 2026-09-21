package br.fai.faitec.petlink2026.ports_and_adapters.port.service.vaccine;

import br.fai.faitec.petlink2026.domain.vaccine.VaccineAlertModel;

import java.util.List;

public interface VaccineNotificationService {

    List<VaccineAlertModel> findAlertsByOwnerId(final int ownerId);
}
