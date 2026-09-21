package br.fai.faitec.petlink2026.controller;

import br.fai.faitec.petlink2026.domain.animal.FarmAnimalModel;
import br.fai.faitec.petlink2026.domain.vaccine.VaccineModel;
import br.fai.faitec.petlink2026.dto.animal.CreateFarmAnimalDto;
import br.fai.faitec.petlink2026.dto.animal.UpdateAnimalFarmDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.animal.FarmAnimalService;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.tools.VaccinationCardPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/animal")
public class FarmAnimalRestController {

    @Autowired
    private FarmAnimalService farmAnimalService;

    @Autowired
    private VaccinationCardPdfService vaccinationCardPdfService;

    //     BUSCAR TODOS OS FARM ANIMALS
    @GetMapping
    public ResponseEntity<List<FarmAnimalModel>> getFarmAnimals() {
        List<FarmAnimalModel> farmAnimals = farmAnimalService.findAll();
        return ResponseEntity.ok(farmAnimals);
    }

    // BUSCAR FARM ANIMAL PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<FarmAnimalModel> getFarmAnimalById(@PathVariable final int id) {
        FarmAnimalModel farmAnimalModel = farmAnimalService.findById(id);
        return farmAnimalModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(farmAnimalModel);
    }

    // CRIAR FARM ANIMAL
    @PostMapping
    public ResponseEntity<FarmAnimalModel> createFarmAnimal(@RequestBody final CreateFarmAnimalDto createFarmAnimalDto) {
        FarmAnimalModel farmAnimalModel = createFarmAnimalDto.toFarmAnimalModel();
        final int id = farmAnimalService.create(farmAnimalModel);

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
    public ResponseEntity<Void> updateFarmAnimal(@PathVariable final int id, @RequestBody final UpdateAnimalFarmDto UpdateAnimalFarmDto) {

        FarmAnimalModel farmAnimalModel = UpdateAnimalFarmDto.toFarmAnimalModel();

        boolean response = farmAnimalService.update(id, farmAnimalModel);

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmAnimal(@PathVariable final int id) {
        FarmAnimalModel farmAnimalModel = farmAnimalService.findById(id);

        if (farmAnimalModel == null) {
            return ResponseEntity.notFound().build();
        }

        farmAnimalService.delete(id);
        return ResponseEntity.noContent().build();
    }


    //     BUSCAR TODAS AS VACINAS DO FARM ANIMAL
    @GetMapping("/{farmAnimalId}/vaccine")
    public ResponseEntity<List<VaccineModel>> getEntitiesByFarmAnimalId(@PathVariable final int farmAnimalId) {
        List<VaccineModel> entities = farmAnimalService.showAllVaccineByAnimalId(farmAnimalId);
        return ResponseEntity.ok(entities);
    }

    //     BUSCAR UMA DAS VACINAS DO FARM ANIMAL
    @GetMapping("/{farmAnimalId}/vaccine/{vaccineId}")
    public ResponseEntity<VaccineModel> getEntitiesByFarmAnimalIdAndVaccineId(
            @PathVariable final int farmAnimalId,
            @PathVariable final int vaccineId) {
        VaccineModel vaccineModel = farmAnimalService.findVaccineByAnimalId(farmAnimalId, vaccineId);
        return vaccineModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(vaccineModel);
    }

    //     GERAR PDF DO CARTAO DE VACINACAO (funciona para qualquer animal, pet ou de fazenda)
    @GetMapping("/{farmAnimalId}/vaccination-card")
    public ResponseEntity<byte[]> getVaccinationCardPdf(@PathVariable final int farmAnimalId) throws IOException {
        return VaccinationCardResponses.build(vaccinationCardPdfService, farmAnimalId);
    }

}