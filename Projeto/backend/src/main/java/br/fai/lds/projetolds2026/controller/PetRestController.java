package br.fai.lds.projetolds2026.controller;

import br.fai.lds.projetolds2026.domain.pet.PetModel;
import br.fai.lds.projetolds2026.domain.vaccine.VaccineModel;
import br.fai.lds.projetolds2026.dto.pet.CreatePetDto;
import br.fai.lds.projetolds2026.dto.vaccine.CreateVaccineDto;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.pet.PetService;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.vaccine.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pet")
public class PetRestController {

    @Autowired
    private PetService petService;

    @Autowired
    private VaccineService vaccineService;

    //     BUSCAR TODOS OS PETS
    @GetMapping
    public ResponseEntity<List<PetModel>> getPets() {
        List<PetModel> pets = petService.findAll();
        return ResponseEntity.ok(pets);
    }

    // BUSCAR PET PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<PetModel> getPetById(@PathVariable final int id) {
        PetModel petModel = petService.findById(id);
        return petModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(petModel);
    }

    // CRIAR PET
    @PostMapping
    public ResponseEntity<PetModel> createPet(@RequestBody final CreatePetDto createPetDto) {
        PetModel petModel = createPetDto.toPetModel();
        final int id = petService.create(petModel);

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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePet(
            @PathVariable final int id,
            @RequestBody final PetModel petModel) {

        boolean response = petService.update(id, petModel);

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable final int id) {
        PetModel petModel = petService.findById(id);

        if (petModel == null) {
            return ResponseEntity.notFound().build();
        }

        petService.delete(id);
        return ResponseEntity.noContent().build();
    }


    //     BUSCAR TODAS AS VACINAS DO PET
    @GetMapping("/{petId}/vaccine")
    public ResponseEntity<List<VaccineModel>> getEntitiesByPetId(@PathVariable final int petId) {
        List<VaccineModel> entities = petService.showAllVaccineByPetId(petId);
        return ResponseEntity.ok(entities);
    }

    //     BUSCAR UMA DAS VACINAS DO PET
    @GetMapping("/{petId}/vaccine/{vaccineId}")
    public ResponseEntity<VaccineModel> getEntitiesByPetIdAndVaccineId(@PathVariable final int petId, @PathVariable final int vaccineId) {
        VaccineModel vaccineModel = petService.findVaccineByPetId(petId, vaccineId);
        return ResponseEntity.ok(vaccineModel);
    }

    //VACINAS
    //BUSCAR TODAS AS VACINAS
    @GetMapping("vaccine")
    public ResponseEntity<List<VaccineModel>> getVaccines() {
        List<VaccineModel> vaccines = vaccineService.findAll();
        return ResponseEntity.ok(vaccines);
    }


    // BUSCAR PET PELO ID
    @GetMapping("vaccine/{id}")
    public ResponseEntity<VaccineModel> getVaccineById(@PathVariable final int id) {
        VaccineModel vaccineModel = vaccineService.findById(id);
        return vaccineModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(vaccineModel);
    }

    // CRIAR PET
    @PostMapping("vaccine")
    public ResponseEntity<PetModel> create(@RequestBody final CreateVaccineDto createVaccineDto) {
        VaccineModel vaccineModel = createVaccineDto.toVaccineModel();
        final int id = vaccineService.create(vaccineModel);

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

    @PutMapping("vaccine/{id}")
    public ResponseEntity<Void> update(
            @PathVariable final int id,
            @RequestBody final VaccineModel vaccineModel) {

        boolean response = vaccineService.update(id, vaccineModel);

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("vaccine/{id}")
    public ResponseEntity<Void> delete(@PathVariable final int id) {
        VaccineModel vaccineModel = vaccineService.findById(id);

        if (vaccineModel == null) {
            return ResponseEntity.notFound().build();
        }

        vaccineService.delete(id);
        return ResponseEntity.noContent().build();
    }


}