package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.UserCategoryDTO;
import com.dio.santander.apimanagerpoints.services.UserCategoryService;
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
@RequestMapping("/api/v1/usercategories")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserCategoryController {
    private UserCategoryService userCategoryService;

    @GetMapping
    public List<UserCategoryDTO> findAll() {
        return userCategoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCategoryDTO> find(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(userCategoryService.find(id));
    }

    @PostMapping
    public ResponseEntity<UserCategoryDTO> insert(@RequestBody @Valid UserCategoryDTO objDto) {
        objDto = userCategoryService.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objDto.getId()).toUri();
        return ResponseEntity.created(uri).body(objDto);
    }

    @PutMapping
    public ResponseEntity<UserCategoryDTO> update(@RequestBody @Valid UserCategoryDTO objDto) {
        return ResponseEntity.ok().body(userCategoryService.update(objDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserCategoryDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "description") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<UserCategoryDTO> listDto = userCategoryService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
