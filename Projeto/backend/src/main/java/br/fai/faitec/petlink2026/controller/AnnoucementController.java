package br.fai.faitec.petlink2026.controller;

import br.fai.faitec.petlink2026.domain.annoucement.AnnoucementModel;
import br.fai.faitec.petlink2026.dto.annoucement.CreateAnnoucementDto;
import br.fai.faitec.petlink2026.dto.annoucement.ReadAnnouncementDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.annoucement.AnnoucementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnoucementController {

    @Autowired
    private AnnoucementService annoucementService;

    // BUSCAR TODOS OS ANUNCIOS
    @GetMapping
    public ResponseEntity<List<ReadAnnouncementDto>> getAnnoucements() {
        List<ReadAnnouncementDto> annoucements = annoucementService.getAnnouncements();
        return ResponseEntity.ok(annoucements);
    }

    // BUSCAR ANUNCIO PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<ReadAnnouncementDto> getAnnoucementById(@PathVariable final int id) {
        ReadAnnouncementDto annoucement = annoucementService.getAnnouncementById(id);
        return annoucement == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(annoucement);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ReadAnnouncementDto>> getAnnouncementsByUserId(@PathVariable final int userId) {
        List<ReadAnnouncementDto> announcements = annoucementService.getAnnouncementsByUserId(userId);
        return ResponseEntity.ok(announcements);
    }

    // CRIAR ANUNCIO
    @PostMapping
    public ResponseEntity<AnnoucementModel> createAnnoucement(@RequestBody final CreateAnnoucementDto createAnnoucementDto) {
        AnnoucementModel annoucementModel = createAnnoucementDto.toAnnoucementModel();
        final int id = annoucementService.create(annoucementModel);

        if (id == 0) {
            return ResponseEntity.badRequest().build();
        }

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    // APAGAR ANUNCIO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnoucement(@PathVariable final int id) {
        AnnoucementModel annoucementModel = annoucementService.findById(id);

        if (annoucementModel == null) {
            return ResponseEntity.notFound().build();
        }

        annoucementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}