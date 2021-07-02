package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.dtos.CalendarDTO;
import com.dio.santander.apimanagerpoints.services.CalendarService;
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
@RequestMapping("/api/v1/calendars")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CalendarController {
    private CalendarService calendarService;

    @GetMapping
    public List<CalendarDTO> findAll() {
        return calendarService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarDTO> find(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(calendarService.find(id));
    }

    @PostMapping
    public ResponseEntity<CalendarDTO> insert(@RequestBody @Valid CalendarDTO objDto) {
        objDto = calendarService.insert(objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objDto.getId()).toUri();
        return ResponseEntity.created(uri).body(objDto);
    }

    @PutMapping
    public ResponseEntity<CalendarDTO> update(@RequestBody @Valid CalendarDTO objDto) {
        return ResponseEntity.ok().body(calendarService.update(objDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        calendarService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CalendarDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "description") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<CalendarDTO> listDto = calendarService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok(listDto);
    }
}
