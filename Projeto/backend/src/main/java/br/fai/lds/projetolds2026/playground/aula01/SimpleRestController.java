package br.fai.lds.projetolds2026.playground.aula01;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simple")
public class SimpleRestController {

    @GetMapping
    public ResponseEntity<String> sayGoodNight(){
        System.out.println("Boa noite");

        return ResponseEntity.ok("boa noite");
        //return ResponseEntity.badRequest().build();
        //return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
