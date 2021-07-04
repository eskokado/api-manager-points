package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.UserDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.UserDTO;
import com.dio.santander.apimanagerpoints.mappers.UserMapper;
import com.dio.santander.apimanagerpoints.models.User;
import com.dio.santander.apimanagerpoints.repositories.UserRepository;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectAlreadyRegisteredException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_USER_ID = 2L;

    @Mock
    private UserRepository userRepository;

    private UserMapper userMapper = UserMapper.INSTANCE;

    @InjectMocks
    private UserService userService;

    @Test
    void whenUserInformedIsNotExistingThenItShouldBeCreated() {
        // given
        UserDTO expectedUserDTO = UserDTOBuilder.builder().build().toUserDTO();
        User expectedUser = userMapper.toModel(expectedUserDTO);

        // when
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.empty());
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        // then
        UserDTO createUserDTO = userService.insert(expectedUserDTO);

        assertThat(createUserDTO, is(equalTo(expectedUserDTO)));
    }

    @Test
    void whenAlreadyRegisteredUserInformedThenAnExceptionShouldBeThrown() {
        // given
        UserDTO expectedUserDTO = UserDTOBuilder.builder().build().toUserDTO();
        User expectedUser = userMapper.toModel(expectedUserDTO);

        // when
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(expectedUser));

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> userService.insert(expectedUserDTO));
    }

    @Test
    void whenValidUserIdIsGivenThenReturnAnUser() {
        // given
        UserDTO expectedUserDTO = UserDTOBuilder.builder().build().toUserDTO();
        User expectedUser = userMapper.toModel(expectedUserDTO);

        // when
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(expectedUser));

        // then
        UserDTO foundUserDTO = userService.find(VALID_USER_ID);

        assertThat(foundUserDTO, is(equalTo(expectedUserDTO)));
    }

    @Test
    void whenNotRegisteredUserIdGivenThenThrowAnException() {
        // when
        when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> userService.find(INVALID_USER_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidUserGivenThenReturnAUserUpdated() {
        // given
        UserDTO expectedUserDTO = UserDTOBuilder.builder().build().toUserDTO();
        User expectedUser = userMapper.toModel(expectedUserDTO);

        // when
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(expectedUser));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        // then
        UserDTO updateUserDTO = userService.update(expectedUserDTO);

        assertThat(updateUserDTO, is(equalTo(expectedUserDTO)));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingUserThenAnExceptionShouldBeThrown() {
        // given
        UserDTO expectedUserDTO = UserDTOBuilder.builder().build().toUserDTO();

        // when
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> userService.update(expectedUserDTO));
    }

    @Test
    void whenListUserIsCalledThenReturnAListOfUser() {
        // given
        UserDTO expectedUserDTO = UserDTOBuilder.builder().build().toUserDTO();
        User expectedUser = userMapper.toModel(expectedUserDTO);

        // when
        when(userRepository.findAll()).thenReturn(Collections.singletonList(expectedUser));

        // then
        List<UserDTO> listUserDTO = userService.findAll();

        assertThat(listUserDTO, is(not(empty())));
        assertThat(listUserDTO.get(0), is(equalTo(expectedUserDTO)));
    }

    @Test
    void whenListUserIsCalledThenReturnAnEmptyListOfUser() {
        // when
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<UserDTO> listUserDTO = userService.findAll();

        assertThat(listUserDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAUserShouldBeDeleted() {
        // given
        UserDTO expectedUserDTO = UserDTOBuilder.builder().build().toUserDTO();
        User expectedUser = userMapper.toModel(expectedUserDTO);

        //  when
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(expectedUser));
        doNothing().when(userRepository).deleteById(VALID_USER_ID);

        // then
        userService.delete(VALID_USER_ID);

        verify(userRepository, times(1)).findById(VALID_USER_ID);
        verify(userRepository, times(1)).deleteById(VALID_USER_ID);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingUserThenanExceptionShouldBeThrown() {
        // when
        when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> userService.delete(INVALID_USER_ID));
    }
}
