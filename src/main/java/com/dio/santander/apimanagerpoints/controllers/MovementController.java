package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.MovementDTO;
import com.dio.santander.apimanagerpoints.services.MovementService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movements")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MovementController {
    private MovementService movementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MovementDTO> insert(@RequestBody @Valid MovementDTO objDto) {
        objDto = movementService.insert(objDto);
        return ResponseEntity.ok(objDto);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<MovementDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "dateOfIn") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<MovementDTO> listDto = movementService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
