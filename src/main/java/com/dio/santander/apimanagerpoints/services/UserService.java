package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.UserDTO;
import com.dio.santander.apimanagerpoints.mappers.UserMapper;
import com.dio.santander.apimanagerpoints.models.User;
import com.dio.santander.apimanagerpoints.repositories.UserRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserDTO insert(UserDTO objDto) {
        verifyIfIsAlreadyRegistered(objDto.getId());
        User objToSave = userMapper.toModel(objDto);
        User objSaved = userRepository.save(objToSave);
        return userMapper.toDto(objSaved);
    }

    public UserDTO update(UserDTO objDto) {
        find(objDto.getId());
        User objToSave = userMapper.toModel(objDto);
        User objSaved = userRepository.save(objToSave);
        return userMapper.toDto(objSaved);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    public UserDTO find(Long id) {
        User obj = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Objeto não encontrado! Id: " + id + ", Tipo: " + User.class.getName()
                ));
        return userMapper.toDto(obj);
    }

    public void delete(Long id) {
        find(id);
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma jornada de trabalho que possui dependências");
        }
    }

    public Page<UserDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return userRepository.findAll(pageRequest).map(userMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long id) {
        Optional<User> optObj = userRepository.findById(id);
        if (optObj.isPresent()) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! Id: " + id + ", Tipo: " + User.class.getName()
            );
        }
    }
}
