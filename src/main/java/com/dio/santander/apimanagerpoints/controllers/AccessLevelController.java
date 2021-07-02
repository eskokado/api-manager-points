package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.AccessLevelDTO;
import com.dio.santander.apimanagerpoints.services.AccessLevelService;
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
@RequestMapping("/api/v1/access_levels")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AccessLevelController {
    private AccessLevelService accessLevelService;

    @GetMapping
    public List<AccessLevelDTO> findAll() {
        return accessLevelService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessLevelDTO> find(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(accessLevelService.find(id));
    }

    @PostMapping
    public ResponseEntity<AccessLevelDTO> insert(@RequestBody @Valid AccessLevelDTO objDto) {
        objDto = accessLevelService.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objDto.getId()).toUri();
        return ResponseEntity.created(uri).body(objDto);
    }

    @PutMapping
    public ResponseEntity<AccessLevelDTO> update(@RequestBody @Valid AccessLevelDTO objDto) {
        return ResponseEntity.ok().body(accessLevelService.update(objDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accessLevelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<AccessLevelDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "description") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<AccessLevelDTO> listDto = accessLevelService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
