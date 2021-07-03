package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.UserCategoryDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.UserCategoryDTO;
import com.dio.santander.apimanagerpoints.mappers.UserCategoryMapper;
import com.dio.santander.apimanagerpoints.models.UserCategory;
import com.dio.santander.apimanagerpoints.repositories.UserCategoryRepository;
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
public class UserCategoryServiceTest {
    private static final Long VALID_USER_CATEGORY_ID = 1L;
    private static final Long INVALID_USER_CATEGORY_ID = 2L;

    @Mock
    private UserCategoryRepository userCategoryRepository;

    private UserCategoryMapper userCategoryMapper = UserCategoryMapper.INSTANCE;

    @InjectMocks
    private UserCategoryService userCategoryService;

    @Test
    void whenUserCategoryInformedIsNotExistingThenItShouldBeCreated() {
        // given
        UserCategoryDTO expectedUserCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
        UserCategory expectedUserCategory = userCategoryMapper.toModel(expectedUserCategoryDTO);

        // when
        when(userCategoryRepository.findById(VALID_USER_CATEGORY_ID)).thenReturn(Optional.empty());
        when(userCategoryRepository.save(expectedUserCategory)).thenReturn(expectedUserCategory);

        // then
        UserCategoryDTO createUserCategoryDTO = userCategoryService.insert(expectedUserCategoryDTO);

        assertThat(createUserCategoryDTO, is(equalTo(expectedUserCategoryDTO)));
    }

    @Test
    void whenAlreadyRegisteredUserCategoryInformedThenAnExceptionShouldBeThrown() {
        // given
        UserCategoryDTO expectedUserCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
        UserCategory expectedUserCategory = userCategoryMapper.toModel(expectedUserCategoryDTO);

        // when
        when(userCategoryRepository.findById(VALID_USER_CATEGORY_ID)).thenReturn(Optional.of(expectedUserCategory));

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> userCategoryService.insert(expectedUserCategoryDTO));
    }

    @Test
    void whenValidUserCategoryIdIsGivenThenReturnAUserCategory() {
        // given
        UserCategoryDTO expectedFoundUserCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
        UserCategory expectedFoundUserCategory = userCategoryMapper.toModel(expectedFoundUserCategoryDTO);

        // when
        when(userCategoryRepository.findById(VALID_USER_CATEGORY_ID)).thenReturn(Optional.of(expectedFoundUserCategory));

        // then
        UserCategoryDTO foundUserCategoryDTO = userCategoryService.find(VALID_USER_CATEGORY_ID);

        assertThat(foundUserCategoryDTO, is(equalTo(expectedFoundUserCategoryDTO)));
    }

    @Test
    void whenNotRegisteredUserCategoryIdIsGivenThenThrowAnException() {
        // when
        when(userCategoryRepository.findById(INVALID_USER_CATEGORY_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> userCategoryService.find(INVALID_USER_CATEGORY_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidUserCategoryGivenThenReturnAUserCategoryUpdated() {
        // given
        UserCategoryDTO expectedUpdatedUserCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
        UserCategory expectedUpdatedUserCategory = userCategoryMapper.toModel(expectedUpdatedUserCategoryDTO);

        // when
        when(userCategoryRepository.findById(VALID_USER_CATEGORY_ID)).thenReturn(Optional.of(expectedUpdatedUserCategory));
        when(userCategoryRepository.save(expectedUpdatedUserCategory)).thenReturn(expectedUpdatedUserCategory);

        // then
        UserCategoryDTO updateUserCategoryDTO = userCategoryService.update(expectedUpdatedUserCategoryDTO);

        assertThat(updateUserCategoryDTO, is(equalTo(expectedUpdatedUserCategoryDTO)));
    }

    @Test
    void whenUpdateIsCalledWhitNotExistingUserCategoryThenReturnAnException(){
        // given
        UserCategoryDTO expectedFoundUserCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();

        // when
        when(userCategoryRepository.findById(VALID_USER_CATEGORY_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> userCategoryService.update(expectedFoundUserCategoryDTO));
    }

    @Test
    void whenListUserCategoryIsCalledThenReturnAListOfUserCategory() {
        // given
        UserCategoryDTO expectedUserCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
        UserCategory expectedUserCategory = userCategoryMapper.toModel(expectedUserCategoryDTO);

        // when
        when(userCategoryRepository.findAll()).thenReturn(Collections.singletonList(expectedUserCategory));

        // then
        List<UserCategoryDTO> listUserCategoryDTO = userCategoryService.findAll();

        assertThat(listUserCategoryDTO, is(not(empty())));
        assertThat(listUserCategoryDTO.get(0), is(equalTo(expectedUserCategoryDTO)));
    }

    @Test
    void whenListUserCategoryIsCalledThenAnEmptyListOfUserCategory() {
        // when
        when(userCategoryRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<UserCategoryDTO> listUserCategoryDTO = userCategoryService.findAll();

        assertThat(listUserCategoryDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAUserCategoryShouldBeDeleted() {
        // given
        UserCategoryDTO expectedDeletedUserCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
        UserCategory expectedDeletedUserCategory = userCategoryMapper.toModel(expectedDeletedUserCategoryDTO);

        // when
        when(userCategoryRepository.findById(VALID_USER_CATEGORY_ID)).thenReturn(Optional.of(expectedDeletedUserCategory));
        doNothing().when(userCategoryRepository).deleteById(VALID_USER_CATEGORY_ID);

        // then
        userCategoryService.delete(VALID_USER_CATEGORY_ID);

        verify(userCategoryRepository, times(1)).findById(VALID_USER_CATEGORY_ID);
        verify(userCategoryRepository, times(1)).deleteById(VALID_USER_CATEGORY_ID);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingUserCategoryThenAnExceptionShouldBeThrown() {
        // when
        when(userCategoryRepository.findById(INVALID_USER_CATEGORY_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> userCategoryService.delete(INVALID_USER_CATEGORY_ID));
    }
}
