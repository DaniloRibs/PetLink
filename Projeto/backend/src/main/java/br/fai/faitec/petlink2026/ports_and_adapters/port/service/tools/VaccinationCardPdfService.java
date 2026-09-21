package br.fai.faitec.petlink2026.ports_and_adapters.port.service.tools;

import java.io.IOException;

public interface VaccinationCardPdfService {

    String generateVaccinationCardPdf(final int animalId, final String outputDirectory) throws IOException;
}
