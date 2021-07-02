package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.CalendarDTO;
import com.dio.santander.apimanagerpoints.mappers.CalendarMapper;
import com.dio.santander.apimanagerpoints.models.Calendar;
import com.dio.santander.apimanagerpoints.repositories.CalendarRepository;
import com.dio.santander.apimanagerpoints.services.exceptions.DataIntegrityException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectAlreadyRegisteredException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final CalendarMapper calendarMapper = CalendarMapper.INSTANCE;

    public CalendarDTO insert(CalendarDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId());
        Calendar objToSave = calendarMapper.toModel(objDto);
        Calendar objSaved = calendarRepository.save(objToSave);
        return calendarMapper.toDto(objSaved);
    }

    public CalendarDTO update(CalendarDTO objDto) {
        find(objDto.getId());
        Calendar objToSave = calendarMapper.toModel(objDto);
        Calendar objSaved = calendarRepository.save(objToSave);
        return calendarMapper.toDto(objSaved);
    }

    public List<CalendarDTO> findAll() {
        return calendarRepository.findAll().stream().map(calendarMapper::toDto).collect(Collectors.toList());
    }

    public CalendarDTO find(Long id) {
        Calendar obj = calendarRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Objeto não encontrado! Id: " + id + ", Tipo: " + Calendar.class.getName()
                ));
        return calendarMapper.toDto(obj);
    }

    public void delete(Long id) {
        find(id);
        try {
            calendarRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma jornada de trabalho que possui dependências");
        }
    }

    public Page<CalendarDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return calendarRepository.findAll(pageRequest).map(calendarMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long id) {
        Optional<Calendar> optObj = calendarRepository.findById(id);
        if (optObj.isPresent()) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! Id: " + id + ", Tipo: " + Calendar.class.getName()
            );
        }
    }
}
