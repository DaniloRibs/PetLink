package br.fai.faitec.petlink2026.controller;


import br.fai.faitec.petlink2026.domain.adoption.AdoptionModel;
import br.fai.faitec.petlink2026.dto.adoption.CreateAdoptionDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.adoption.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/adoption")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    // BUSCAR TODAS AS ADOCOES
    @GetMapping
    public ResponseEntity<List<AdoptionModel>> getAdoptions() {
        List<AdoptionModel> adoptions = adoptionService.findAll();
        return ResponseEntity.ok(adoptions);
    }

    // BUSCAR ADOCAO PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionModel> getAdoptionById(@PathVariable final int id) {
        AdoptionModel adoptionModel = adoptionService.findById(id);
        return adoptionModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(adoptionModel);
    }

    // CRIAR ADOCAO
    @PostMapping
    public ResponseEntity<AdoptionModel> createAdoption(@RequestBody final CreateAdoptionDto createAdoptionDto) {
        AdoptionModel adoptionModel = createAdoptionDto.toAdoptionModel();
        final int id = adoptionService.create(adoptionModel);

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

    // EDITAR ADOCAO
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAdoption(
            @PathVariable final int id,
            @RequestBody final AdoptionModel adoptionModel) {

        boolean response = adoptionService.update(id, adoptionModel);

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    // MARCAR COMO ADOTADO
    @PatchMapping("/{id}/adopt")
    public ResponseEntity<Void> markAsAdopted(@PathVariable final int id) {
        boolean response = adoptionService.markAsAdopted(id);

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    // APAGAR ADOCAO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable final int id) {
        AdoptionModel adoptionModel = adoptionService.findById(id);

        if (adoptionModel == null) {
            return ResponseEntity.notFound().build();
        }

        adoptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}