package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.LocationDTO;
import com.dio.santander.apimanagerpoints.services.LocationService;
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
@RequestMapping("/api/v1/locations")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LocationController {
    private LocationService locationService;

    @GetMapping
    public List<LocationDTO> findAll() {
        return locationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> find(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(locationService.find(id));
    }

    @PostMapping
    public ResponseEntity<LocationDTO> insert(@RequestBody @Valid LocationDTO objDto) {
        objDto = locationService.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objDto.getId()).toUri();
        return ResponseEntity.created(uri).body(objDto);
    }

    @PutMapping
    public ResponseEntity<LocationDTO> update(@RequestBody @Valid LocationDTO objDto) {
        return ResponseEntity.ok().body(locationService.update(objDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<LocationDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "description") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<LocationDTO> listDto = locationService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
