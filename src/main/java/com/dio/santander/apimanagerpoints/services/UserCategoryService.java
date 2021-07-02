package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.UserCategoryDTO;
import com.dio.santander.apimanagerpoints.mappers.UserCategoryMapper;
import com.dio.santander.apimanagerpoints.models.UserCategory;
import com.dio.santander.apimanagerpoints.repositories.UserCategoryRepository;
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
public class UserCategoryService {
    private final UserCategoryRepository userCategoryRepository;
    private final UserCategoryMapper userCategoryMapper = UserCategoryMapper.INSTANCE;

    public UserCategoryDTO insert(UserCategoryDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId());
        UserCategory objToSave = userCategoryMapper.toModel(objDto);
        UserCategory objSaved = userCategoryRepository.save(objToSave);
        return userCategoryMapper.toDto(objSaved);
    }

    public UserCategoryDTO update(UserCategoryDTO objDto) {
        find(objDto.getId());
        UserCategory objToSave = userCategoryMapper.toModel(objDto);
        UserCategory objSaved = userCategoryRepository.save(objToSave);
        return userCategoryMapper.toDto(objSaved);
    }

    public List<UserCategoryDTO> findAll() {
        return userCategoryRepository.findAll().stream().map(userCategoryMapper::toDto).collect(Collectors.toList());
    }

    public UserCategoryDTO find(Long id) {
        UserCategory obj = userCategoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Objeto não encontrado! Id: " + id + ", Tipo: " + UserCategory.class.getName()
                ));
        return userCategoryMapper.toDto(obj);
    }

    public void delete(Long id) {
        find(id);
        try {
            userCategoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma categoria de usuário que possui dependências");
        }
    }

    public Page<UserCategoryDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return userCategoryRepository.findAll(pageRequest).map(userCategoryMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long id) {
        Optional<UserCategory> optObj = userCategoryRepository.findById(id);
        if (optObj.isPresent()) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! Id: " + id + ", Tipo: " + UserCategory.class.getName()
            );
        }
    }
}
