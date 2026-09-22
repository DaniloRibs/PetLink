package br.fai.faitec.petlink2026.controller;


import br.fai.faitec.petlink2026.domain.adoption.AdoptionModel;
import br.fai.faitec.petlink2026.dto.adoption.CreateAdoptionDto;
import br.fai.faitec.petlink2026.dto.adoption.MarkAsAdoptedDto;
import br.fai.faitec.petlink2026.dto.adoption.TransferDecisionDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.adoption.AdoptionService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.transfer.TransferService;
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

    @Autowired
    private TransferService transferService;

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

    // informado em receiverEmail confirmar (ver /transfer/confirm e /transfer/reject).
    @PatchMapping("/{id}/adopt")
    public ResponseEntity<Void> markAsAdopted(@PathVariable final int id, @RequestBody final MarkAsAdoptedDto markAsAdoptedDto) {
        boolean response = transferService.requestTransfer(id, markAsAdoptedDto.getReceiverEmail());

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    // O USUARIO QUE VAI RECEBER O PET ACEITA A TRANSFERENCIA
    @PatchMapping("/{id}/transfer/confirm")
    public ResponseEntity<Void> confirmTransfer(@PathVariable final int id, @RequestBody final TransferDecisionDto transferDecisionDto) {
        boolean response = transferService.confirmTransfer(id, transferDecisionDto.getReceiverId());

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    // O USUARIO QUE VAI RECEBER O PET RECUSA A TRANSFERENCIA
    @PatchMapping("/{id}/transfer/reject")
    public ResponseEntity<Void> rejectTransfer(@PathVariable final int id, @RequestBody final TransferDecisionDto transferDecisionDto) {
        boolean response = transferService.rejectTransfer(id, transferDecisionDto.getReceiverId());

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