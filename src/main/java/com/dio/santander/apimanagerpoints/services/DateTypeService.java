package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.DateTypeDTO;
import com.dio.santander.apimanagerpoints.mappers.DateTypeMapper;
import com.dio.santander.apimanagerpoints.models.DateType;
import com.dio.santander.apimanagerpoints.repositories.DateTypeRepository;
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
public class DateTypeService {
    private final DateTypeRepository dateTypeRepository;
    private final DateTypeMapper dateTypeMapper = DateTypeMapper.INSTANCE;

    public DateTypeDTO insert(DateTypeDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId());
        DateType objToSave = dateTypeMapper.toModel(objDto);
        DateType objSaved = dateTypeRepository.save(objToSave);
        return dateTypeMapper.toDto(objSaved);
    }

    public DateTypeDTO update(DateTypeDTO objDto) {
        find(objDto.getId());
        DateType objToSave = dateTypeMapper.toModel(objDto);
        DateType objSaved = dateTypeRepository.save(objToSave);
        return dateTypeMapper.toDto(objSaved);
    }

    public List<DateTypeDTO> findAll() {
        return dateTypeRepository.findAll().stream().map(dateTypeMapper::toDto).collect(Collectors.toList());
    }

    public DateTypeDTO find(Long id) {
        DateType obj = dateTypeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Objeto não encontrado! Id: " + id + ", Tipo: " + DateType.class.getName()
                ));
        return dateTypeMapper.toDto(obj);
    }

    public void delete(Long id) {
        find(id);
        try {
            dateTypeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir um tipo de data que possui dependências");
        }
    }

    public Page<DateTypeDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return dateTypeRepository.findAll(pageRequest).map(dateTypeMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long id) {
        Optional<DateType> optObj = dateTypeRepository.findById(id);
        if (optObj.isPresent()) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! Id: " + id + ", Tipo: " + DateType.class.getName()
            );
        }
    }
}
