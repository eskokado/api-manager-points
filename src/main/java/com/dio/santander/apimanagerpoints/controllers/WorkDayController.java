package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.WorkDayDTO;
import com.dio.santander.apimanagerpoints.services.WorkDayService;
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
@RequestMapping("/api/v1/workdays")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WorkDayController {
    private WorkDayService workDayService;

    @GetMapping
    public List<WorkDayDTO> findAll() {
        return workDayService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkDayDTO> find(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(workDayService.find(id));
    }

    @PostMapping
    public ResponseEntity<WorkDayDTO> insert(@RequestBody @Valid WorkDayDTO objDto) {
        objDto = workDayService.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objDto.getId()).toUri();
        return ResponseEntity.created(uri).body(objDto);
    }

    @PutMapping
    public ResponseEntity<WorkDayDTO> update(@RequestBody @Valid WorkDayDTO objDto) {
        return ResponseEntity.ok().body(workDayService.update(objDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workDayService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<WorkDayDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "description") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<WorkDayDTO> listDto = workDayService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
