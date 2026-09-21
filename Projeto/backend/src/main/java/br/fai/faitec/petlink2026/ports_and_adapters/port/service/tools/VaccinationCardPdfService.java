package br.fai.faitec.petlink2026.ports_and_adapters.port.service.tools;

import java.io.IOException;

public interface VaccinationCardPdfService {

    /**
     * Gera o PDF da carteira de vacinacao de qualquer animal (pet ou animal de fazenda).
     *
     * @return o caminho completo do arquivo gerado, ou null se nao existir animal com o id informado.
     */
    String generateVaccinationCardPdf(final int animalId, final String outputDirectory) throws IOException;
}
