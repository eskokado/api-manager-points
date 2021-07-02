package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.AccessLevelDTO;
import com.dio.santander.apimanagerpoints.mappers.AccessLevelMapper;
import com.dio.santander.apimanagerpoints.models.AccessLevel;
import com.dio.santander.apimanagerpoints.repositories.AccessLevelRepository;
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
public class AccessLevelService {
    private final AccessLevelRepository accessLevelRepository;
    private final AccessLevelMapper accessLevelMapper = AccessLevelMapper.INSTANCE;

    public AccessLevelDTO insert(AccessLevelDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId());
        AccessLevel objToSave = accessLevelMapper.toModel(objDto);
        AccessLevel objSaved = accessLevelRepository.save(objToSave);
        return accessLevelMapper.toDto(objSaved);
    }

    public AccessLevelDTO update(AccessLevelDTO objDto) {
        find(objDto.getId());
        AccessLevel objToSave = accessLevelMapper.toModel(objDto);
        AccessLevel objSaved = accessLevelRepository.save(objToSave);
        return accessLevelMapper.toDto(objSaved);
    }

    public List<AccessLevelDTO> findAll() {
        return accessLevelRepository.findAll().stream().map(accessLevelMapper::toDto).collect(Collectors.toList());
    }

    public AccessLevelDTO find(Long id) {
        AccessLevel obj = accessLevelRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Objeto não encontrado! Id: " + id + ", Tipo: " + AccessLevel.class.getName()
                ));
        return accessLevelMapper.toDto(obj);
    }

    public void delete(Long id) {
        find(id);
        try {
            accessLevelRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir um nível de acesso que possui dependências");
        }
    }

    public Page<AccessLevelDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return accessLevelRepository.findAll(pageRequest).map(accessLevelMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long id) {
        Optional<AccessLevel> optObj = accessLevelRepository.findById(id);
        if (optObj.isPresent()) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! Id: " + id + ", Tipo: " + AccessLevel.class.getName()
            );
        }
    }
}
