package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.BankOfHourDTO;
import com.dio.santander.apimanagerpoints.services.BankOfHourService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/bank_of_hours")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BankOfHourController {
    private BankOfHourService bankOfHourService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BankOfHourDTO> insert(@RequestBody @Valid BankOfHourDTO objDto) {
        objDto = bankOfHourService.insert(objDto);
        return ResponseEntity.ok(objDto);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<BankOfHourDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "dateWorked") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<BankOfHourDTO> listDto = bankOfHourService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
