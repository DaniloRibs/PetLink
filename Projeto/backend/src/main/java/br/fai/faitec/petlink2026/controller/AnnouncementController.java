package br.fai.faitec.petlink2026.controller;

import br.fai.faitec.petlink2026.domain.announcement.AnnouncementModel;
import br.fai.faitec.petlink2026.dto.announcement.CreateAnnouncementDto;
import br.fai.faitec.petlink2026.dto.announcement.ReadAnnouncementDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.announcement.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // BUSCAR TODOS OS ANUNCIOS
    @GetMapping
    public ResponseEntity<List<ReadAnnouncementDto>> getAnnouncements() {
        List<ReadAnnouncementDto> announcements = announcementService.getAnnouncements();
        return ResponseEntity.ok(announcements);
    }

    // BUSCAR ANUNCIO PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<ReadAnnouncementDto> getAnnouncementById(@PathVariable final int id) {
        ReadAnnouncementDto announcement = announcementService.getAnnouncementById(id);
        return announcement == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(announcement);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ReadAnnouncementDto>> getAnnouncementsByUserId(@PathVariable final int userId) {
        List<ReadAnnouncementDto> announcements = announcementService.getAnnouncementsByUserId(userId);
        return ResponseEntity.ok(announcements);
    }

    // CRIAR ANUNCIO
    @PostMapping
    public ResponseEntity<AnnouncementModel> createAnnouncement(@RequestBody final CreateAnnouncementDto createAnnouncementDto) {
        AnnouncementModel announcementModel = createAnnouncementDto.toAnnouncementModel();
        final int id = announcementService.create(announcementModel);

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

    // ATUALIZAR ANUNCIO
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAnnouncement(
            @PathVariable final int id,
            @RequestBody final CreateAnnouncementDto createAnnouncementDto) {

        AnnouncementModel announcementModel = createAnnouncementDto.toAnnouncementModel();
        boolean response = announcementService.update(id, announcementModel);

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    // APAGAR ANUNCIO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable final int id) {
        AnnouncementModel announcementModel = announcementService.findById(id);

        if (announcementModel == null) {
            return ResponseEntity.notFound().build();
        }

        announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}