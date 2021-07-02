package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.OccurrenceDTO;
import com.dio.santander.apimanagerpoints.services.OccurrenceService;
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
@RequestMapping("/api/v1/occurrences")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OccurrenceController {
    private OccurrenceService occurrenceService;

    @GetMapping
    public List<OccurrenceDTO> findAll() {
        return occurrenceService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OccurrenceDTO> find(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(occurrenceService.find(id));
    }

    @PostMapping
    public ResponseEntity<OccurrenceDTO> insert(@RequestBody @Valid OccurrenceDTO objDto) {
        objDto = occurrenceService.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objDto.getId()).toUri();
        return ResponseEntity.created(uri).body(objDto);
    }

    @PutMapping
    public ResponseEntity<OccurrenceDTO> update(@RequestBody @Valid OccurrenceDTO objDto) {
        return ResponseEntity.ok().body(occurrenceService.update(objDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        occurrenceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<OccurrenceDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "description") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<OccurrenceDTO> listDto = occurrenceService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
