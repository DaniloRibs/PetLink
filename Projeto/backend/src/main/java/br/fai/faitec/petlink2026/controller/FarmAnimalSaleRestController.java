package br.fai.faitec.petlink2026.controller;

import br.fai.faitec.petlink2026.domain.sale.FarmAnimalSaleModel;
import br.fai.faitec.petlink2026.dto.sale.CalculateFarmAnimalSalePriceDto;
import br.fai.faitec.petlink2026.dto.sale.CreateFarmAnimalSaleDto;
import br.fai.faitec.petlink2026.ports_and_adapters.port.service.sale.FarmAnimalSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/animal-sale")
public class FarmAnimalSaleRestController {

    @Autowired
    private FarmAnimalSaleService farmAnimalSaleService;

    //     BUSCAR TODAS AS VENDAS
    @GetMapping
    public ResponseEntity<List<FarmAnimalSaleModel>> getFarmAnimalSales() {
        List<FarmAnimalSaleModel> sales = farmAnimalSaleService.findAll();
        return ResponseEntity.ok(sales);
    }

    // BUSCAR VENDA PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<FarmAnimalSaleModel> getFarmAnimalSaleById(@PathVariable final int id) {
        FarmAnimalSaleModel farmAnimalSaleModel = farmAnimalSaleService.findById(id);
        return farmAnimalSaleModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(farmAnimalSaleModel);
    }

    // CALCULAR O PRECO SUGERIDO POR ARROBA
    @PostMapping("/calculate-price")
    public ResponseEntity<Double> calculateSuggestedPrice(@RequestBody final CalculateFarmAnimalSalePriceDto dto) {
        try {
            double suggestedPrice = farmAnimalSaleService.calculateSuggestedPrice(dto.getFarmAnimalIds(), dto.getPricePerArroba());
            return ResponseEntity.ok(suggestedPrice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // CRIAR VENDA
    @PostMapping
    public ResponseEntity<FarmAnimalSaleModel> createFarmAnimalSale(@RequestBody final CreateFarmAnimalSaleDto createFarmAnimalSaleDto) {

        final FarmAnimalSaleModel farmAnimalSaleModel;
        try {
            farmAnimalSaleModel = createFarmAnimalSaleDto.toFarmAnimalSaleModel();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        final int id = farmAnimalSaleService.create(farmAnimalSaleModel);

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
    public ResponseEntity<Void> updateFarmAnimalSale(
            @PathVariable final int id,
            @RequestBody final FarmAnimalSaleModel farmAnimalSaleModel) {

        boolean response = farmAnimalSaleService.update(id, farmAnimalSaleModel);

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmAnimalSale(@PathVariable final int id) {
        FarmAnimalSaleModel farmAnimalSaleModel = farmAnimalSaleService.findById(id);

        if (farmAnimalSaleModel == null) {
            return ResponseEntity.notFound().build();
        }

        farmAnimalSaleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}