package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.OccurrenceDTO;
import com.dio.santander.apimanagerpoints.mappers.OccurrenceMapper;
import com.dio.santander.apimanagerpoints.models.Occurrence;
import com.dio.santander.apimanagerpoints.repositories.OccurrenceRepository;
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
public class OccurrenceService {
    private final OccurrenceRepository occurrenceRepository;
    private final OccurrenceMapper occurrenceMapper = OccurrenceMapper.INSTANCE;

    public OccurrenceDTO insert(OccurrenceDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId());
        Occurrence objToSave = occurrenceMapper.toModel(objDto);
        Occurrence objSaved = occurrenceRepository.save(objToSave);
        return occurrenceMapper.toDto(objSaved);
    }

    public OccurrenceDTO update(OccurrenceDTO objDto) {
        find(objDto.getId());
        Occurrence objToSave = occurrenceMapper.toModel(objDto);
        Occurrence objSaved = occurrenceRepository.save(objToSave);
        return occurrenceMapper.toDto(objSaved);
    }

    public List<OccurrenceDTO> findAll() {
        return occurrenceRepository.findAll().stream().map(occurrenceMapper::toDto).collect(Collectors.toList());
    }

    public OccurrenceDTO find(Long id) {
        Occurrence obj = occurrenceRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Objeto não encontrado! Id: " + id + ", Tipo: " + Occurrence.class.getName()
                ));
        return occurrenceMapper.toDto(obj);
    }

    public void delete(Long id) {
        find(id);
        try {
            occurrenceRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma ocorrência que possui dependências");
        }
    }

    public Page<OccurrenceDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return occurrenceRepository.findAll(pageRequest).map(occurrenceMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long id) {
        Optional<Occurrence> optObj = occurrenceRepository.findById(id);
        if (optObj.isPresent()) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! Id: " + id + ", Tipo: " + Occurrence.class.getName()
            );
        }
    }
}
