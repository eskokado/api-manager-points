package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.UserDTO;
import com.dio.santander.apimanagerpoints.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private UserService userService;

    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> find(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(userService.find(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@RequestBody @Valid UserDTO objDto) {
        objDto = userService.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objDto.getId()).toUri();
        return ResponseEntity.created(uri).body(objDto);
    }

    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserDTO objDto) {
        return ResponseEntity.ok().body(userService.update(objDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<UserDTO> listDto = userService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
