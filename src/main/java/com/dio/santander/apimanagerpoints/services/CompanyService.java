package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.CompanyDTO;
import com.dio.santander.apimanagerpoints.mappers.CompanyMapper;
import com.dio.santander.apimanagerpoints.models.Company;
import com.dio.santander.apimanagerpoints.repositories.CompanyRepository;
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
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    public CompanyDTO insert(CompanyDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId());
        Company objToSave = companyMapper.toModel(objDto);
        Company objSaved = companyRepository.save(objToSave);
        return companyMapper.toDto(objSaved);
    }

    public CompanyDTO update(CompanyDTO objDto) {
        find(objDto.getId());
        Company objToSave = companyMapper.toModel(objDto);
        Company objSaved = companyRepository.save(objToSave);
        return companyMapper.toDto(objSaved);
    }

    public List<CompanyDTO> findAll() {
        return companyRepository.findAll().stream().map(companyMapper::toDto).collect(Collectors.toList());
    }

    public CompanyDTO find(Long id) {
        Company obj = companyRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Objeto não encontrado! Id: " + id + ", Tipo: " + Company.class.getName()
                ));
        return companyMapper.toDto(obj);
    }

    public void delete(Long id) {
        find(id);
        try {
            companyRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma jornada de trabalho que possui dependências");
        }
    }

    public Page<CompanyDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return companyRepository.findAll(pageRequest).map(companyMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long id) {
        Optional<Company> optObj = companyRepository.findById(id);
        if (optObj.isPresent()) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! Id: " + id + ", Tipo: " + Company.class.getName()
            );
        }
    }
}
