package br.fai.lds.projetolds2026.playground.aula01;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/good")
public class GoodEveningRestController {

    @GetMapping("evening")
    public ResponseEntity<String> sayGoodEvening(){
        System.out.println("Good Evening");

        return ResponseEntity.ok("good evening");
    }
}
