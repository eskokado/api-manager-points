package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.DateTypeDTO;
import com.dio.santander.apimanagerpoints.services.DateTypeService;
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
@RequestMapping("/api/v1/date_types")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DateTypeController {
    private DateTypeService dateTypeService;

    @GetMapping
    public List<DateTypeDTO> findAll() {
        return dateTypeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DateTypeDTO> find(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(dateTypeService.find(id));
    }

    @PostMapping
    public ResponseEntity<DateTypeDTO> insert(@RequestBody @Valid DateTypeDTO objDto) {
        objDto = dateTypeService.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objDto.getId()).toUri();
        return ResponseEntity.created(uri).body(objDto);
    }

    @PutMapping
    public ResponseEntity<DateTypeDTO> update(@RequestBody @Valid DateTypeDTO objDto) {
        return ResponseEntity.ok().body(dateTypeService.update(objDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dateTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<DateTypeDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "description") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<DateTypeDTO> listDto = dateTypeService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
