package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.MovementDTO;
import com.dio.santander.apimanagerpoints.mappers.MovementMapper;
import com.dio.santander.apimanagerpoints.models.Movement;
import com.dio.santander.apimanagerpoints.models.MovementPK;
import com.dio.santander.apimanagerpoints.repositories.MovementRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MovementService {
    private final MovementRepository movementRepository;
    private final MovementMapper movementMapper = MovementMapper.INSTANCE;

    @Transactional
    public MovementDTO insert(MovementDTO objDto) {
        Movement objToSave = movementMapper.toModel(objDto);
        Movement objSaved = movementRepository.save(objToSave);
        return movementMapper.toDto(objSaved);
    }

    public Page<MovementDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return movementRepository.findAll(pageRequest).map(movementMapper::toDto);
    }
}
