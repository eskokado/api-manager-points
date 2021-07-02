package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.WorkDayDTO;
import com.dio.santander.apimanagerpoints.mappers.WorkDayMapper;
import com.dio.santander.apimanagerpoints.models.WorkDay;
import com.dio.santander.apimanagerpoints.repositories.WorkDayRepository;
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
public class WorkDayService {
    private final WorkDayRepository workDayRepository;
    private final WorkDayMapper workDayMapper = WorkDayMapper.INSTANCE;

    public WorkDayDTO insert(WorkDayDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId());
        WorkDay objToSave = workDayMapper.toModel(objDto);
        WorkDay objSaved = workDayRepository.save(objToSave);
        return workDayMapper.toDto(objSaved);
    }

    public WorkDayDTO update(WorkDayDTO objDto) {
        find(objDto.getId());
        WorkDay objToSave = workDayMapper.toModel(objDto);
        WorkDay objSaved = workDayRepository.save(objToSave);
        return workDayMapper.toDto(objSaved);
    }

    public List<WorkDayDTO> findAll() {
        return workDayRepository.findAll().stream().map(workDayMapper::toDto).collect(Collectors.toList());
    }

    public WorkDayDTO find(Long id) {
        WorkDay obj = workDayRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Objeto não encontrado! Id: " + id + ", Tipo: " + WorkDay.class.getName()
                ));
        return workDayMapper.toDto(obj);
    }

    public void delete(Long id) {
        find(id);
        try {
            workDayRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma jornada de trabalho que possui dependências");
        }
    }

    public Page<WorkDayDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return workDayRepository.findAll(pageRequest).map(workDayMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long id) {
        Optional<WorkDay> optObj = workDayRepository.findById(id);
        if (optObj.isPresent()) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! Id: " + id + ", Tipo: " + WorkDay.class.getName()
            );
        }
    }
}
