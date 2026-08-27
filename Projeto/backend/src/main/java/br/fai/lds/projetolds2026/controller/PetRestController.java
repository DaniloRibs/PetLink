package br.fai.lds.projetolds2026.controller;

import br.fai.lds.projetolds2026.domain.pet.PetModel;
import br.fai.lds.projetolds2026.dto.pet.CreatePetDto;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.pet.PetService;
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

    // BUSCAR TODOS OS PETS
    @GetMapping
    public ResponseEntity<List<PetModel>> getEntities() {
        List<PetModel> entities = petService.findALl();
        return ResponseEntity.ok(entities);
    }

    // BUSCAR PET PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<PetModel> getEntityById(@PathVariable final int id) {
        PetModel petModel = petService.findById(id);
        return petModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(petModel);
    }

    // CRIAR PET
    @PostMapping
    public ResponseEntity<PetModel> create(@RequestBody final CreatePetDto createPetDto) {
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
    public ResponseEntity<Void> update(
            @PathVariable final int id,
            @RequestBody final PetModel petModel) {

        boolean response = petService.update(id, petModel);

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    // ATUALIZAR DONO
    @PutMapping("/{id}/{oldOwner}/{newOwner}")
    public ResponseEntity<Void> updateOwner(
            @PathVariable final int id,
            @PathVariable final int oldOwner,
            @PathVariable final int newOwner) {

        boolean response = petService.updateOwner(id, oldOwner, newOwner);
        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final int id) {
        PetModel petModel = petService.findById(id);

        if (petModel == null) {
            return ResponseEntity.notFound().build();
        }

        petService.delete(id);
        return ResponseEntity.noContent().build();
    }
}