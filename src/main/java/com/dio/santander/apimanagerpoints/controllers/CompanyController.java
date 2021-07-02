package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.CompanyDTO;
import com.dio.santander.apimanagerpoints.services.CompanyService;
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
@RequestMapping("/api/v1/companies")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyController {
    private CompanyService companyService;

    @GetMapping
    public List<CompanyDTO> findAll() {
        return companyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> find(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(companyService.find(id));
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> insert(@RequestBody @Valid CompanyDTO objDto) {
        objDto = companyService.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objDto.getId()).toUri();
        return ResponseEntity.created(uri).body(objDto);
    }

    @PutMapping
    public ResponseEntity<CompanyDTO> update(@RequestBody @Valid CompanyDTO objDto) {
        return ResponseEntity.ok().body(companyService.update(objDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CompanyDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "description") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<CompanyDTO> listDto = companyService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
