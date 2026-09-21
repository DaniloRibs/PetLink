package br.fai.faitec.petlink2026.controller;

import br.fai.faitec.petlink2026.ports_and_adapters.port.service.tools.VaccinationCardPdfService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

final class VaccinationCardResponses {

    private VaccinationCardResponses() {
    }

    static ResponseEntity<byte[]> build(final VaccinationCardPdfService service, final int animalId) throws IOException {
        final String outputDirectory = System.getProperty("java.io.tmpdir") + File.separator;
        final String fullPath = service.generateVaccinationCardPdf(animalId, outputDirectory);

        if (fullPath == null) {
            return ResponseEntity.notFound().build();
        }

        final File pdfFile = new File(fullPath);
        final byte[] content;

        try {
            content = Files.readAllBytes(pdfFile.toPath());
        } finally {
            Files.deleteIfExists(pdfFile.toPath());
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(pdfFile.getName()).build());

        return ResponseEntity.ok().headers(headers).body(content);
    }
}
