package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.MovementDTO;
import com.dio.santander.apimanagerpoints.mappers.MovementMapper;
import com.dio.santander.apimanagerpoints.models.Movement;
import com.dio.santander.apimanagerpoints.models.WorkDay;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MovementService {
    private final MovementRepository movementRepository;
    private final MovementMapper movementMapper = MovementMapper.INSTANCE;

    public MovementDTO insert(MovementDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId().getMovementId(), objDto.getId().getUserId());
        Movement objToSave = movementMapper.toModel(objDto);
        Movement objSaved = movementRepository.save(objToSave);
        return movementMapper.toDto(objSaved);
    }

    public MovementDTO update(MovementDTO objDto) {
        find(objDto.getId().getMovementId(), objDto.getId().getUserId());
        Movement objToSave = movementMapper.toModel(objDto);
        Movement objSaved = movementRepository.save(objToSave);
        return movementMapper.toDto(objSaved);
    }

    public List<MovementDTO> findAll() {
        return movementRepository.findAll().stream().map(movementMapper::toDto).collect(Collectors.toList());
    }

    public MovementDTO find(Long movementId, Long userId) {
        Movement obj = movementRepository.findByPK(movementId, userId);
        if (obj == null) {
            throw new ObjectNotFoundException(
                    "Objeto não encontrado! MovementId: " + movementId +
                            ", UserId: " + userId +
                            ", Tipo: " + WorkDay.class.getName()
            );
        }
        return movementMapper.toDto(obj);
    }

    public void delete(Long movementId, Long userId) {
        find(movementId, userId);
        Movement obj = movementRepository.findByPK(movementId, userId);
        try {
            movementRepository.delete(obj);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir um movimento que possui dependências");
        }
    }

    public Page<MovementDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return movementRepository.findAll(pageRequest).map(movementMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long movementId, Long userId) {
        Movement obj = movementRepository.findByPK(movementId, userId);
        if (obj != null) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! MovementId: " + movementId +
                            ", UserId: " + userId +
                            ", Tipo: " + WorkDay.class.getName()
            );
        }
    }
}
