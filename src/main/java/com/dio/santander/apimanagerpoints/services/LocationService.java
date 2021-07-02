package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.LocationDTO;
import com.dio.santander.apimanagerpoints.mappers.LocationMapper;
import com.dio.santander.apimanagerpoints.models.Location;
import com.dio.santander.apimanagerpoints.repositories.LocationRepository;
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
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper = LocationMapper.INSTANCE;

    public LocationDTO insert(LocationDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId());
        Location objToSave = locationMapper.toModel(objDto);
        Location objSaved = locationRepository.save(objToSave);
        return locationMapper.toDto(objSaved);
    }

    public LocationDTO update(LocationDTO objDto) {
        find(objDto.getId());
        Location objToSave = locationMapper.toModel(objDto);
        Location objSaved = locationRepository.save(objToSave);
        return locationMapper.toDto(objSaved);
    }

    public List<LocationDTO> findAll() {
        return locationRepository.findAll().stream().map(locationMapper::toDto).collect(Collectors.toList());
    }

    public LocationDTO find(Long id) {
        Location obj = locationRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Objeto não encontrado! Id: " + id + ", Tipo: " + Location.class.getName()
                ));
        return locationMapper.toDto(obj);
    }

    public void delete(Long id) {
        find(id);
        try {
            locationRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma jornada de trabalho que possui dependências");
        }
    }

    public Page<LocationDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return locationRepository.findAll(pageRequest).map(locationMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long id) {
        Optional<Location> optObj = locationRepository.findById(id);
        if (optObj.isPresent()) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! Id: " + id + ", Tipo: " + Location.class.getName()
            );
        }
    }
}
