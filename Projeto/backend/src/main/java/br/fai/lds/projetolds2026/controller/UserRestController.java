package br.fai.lds.projetolds2026.controller;

import br.fai.lds.projetolds2026.domain.UserModel;
import br.fai.lds.projetolds2026.dto.CreateUserDto;
import br.fai.lds.projetolds2026.dto.UpdatePasswordDto;
import br.fai.lds.projetolds2026.dto.UpdateUserNameDto;
import br.fai.lds.projetolds2026.ports_and_adapters.port.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    // Listar usuários
    @GetMapping
    public ResponseEntity<List<UserModel>> getEntities(){
        List<UserModel> entities = userService.findALl();
        return ResponseEntity.ok(entities);
    }

    //buscar pelo id
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getEntityById(@PathVariable final int id) {
        UserModel userModel = userService.findById(id);

        return userModel == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(userModel);
    }

    // deletar pelo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final int id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserModel> update(@PathVariable final int id, @RequestBody final UpdateUserNameDto updateUserNameDto){

        final UserModel userModel = updateUserNameDto.toUserModel();

        boolean response = userService.update(id, userModel);

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


    @PostMapping
    public ResponseEntity<UserModel> create(@RequestBody final CreateUserDto createUserDto) {

        UserModel userModel = createUserDto.toUserModel();

        final int id = userService.create(userModel);
        if (id == 0){
            return ResponseEntity.badRequest().build();
        }

        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/").buildAndExpand(id).toUri();

        return ResponseEntity.created(uri).build();
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<UserModel> getEntityByEmail(@PathVariable final String email){
        final UserModel entity = userService.findByEmail(email);
        if(entity == null) {
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(entity);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody final UpdatePasswordDto updatePasswordDto){
        final boolean response = userService.updatePassword(updatePasswordDto.getId(), updatePasswordDto.getOldPassword(), updatePasswordDto.getNewPassword());

        return response ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

}
