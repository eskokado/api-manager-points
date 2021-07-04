package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.AccessLevelDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.AccessLevelDTO;
import com.dio.santander.apimanagerpoints.mappers.AccessLevelMapper;
import com.dio.santander.apimanagerpoints.models.AccessLevel;
import com.dio.santander.apimanagerpoints.repositories.AccessLevelRepository;
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
public class AccessLevelServiceTest {
    private static final Long VALID_ACCESS_LEVEL_ID = 1L;
    private static final Long INVALID_ACCESS_LEVEL_ID = 2L;

    @Mock
    private AccessLevelRepository accessLevelRepository;

    private AccessLevelMapper accessLevelMapper = AccessLevelMapper.INSTANCE;

    @InjectMocks
    private AccessLevelService accessLevelService;

    @Test
    void whenAccessLevelInformedIsNotExistingThenItShouldBeCreated() {
        // given
        AccessLevelDTO expectedAccessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
        AccessLevel expectedAccessLevel = accessLevelMapper.toModel(expectedAccessLevelDTO);

        // when
        when(accessLevelRepository.findById(VALID_ACCESS_LEVEL_ID)).thenReturn(Optional.empty());
        when(accessLevelRepository.save(expectedAccessLevel)).thenReturn(expectedAccessLevel);

        // then
        AccessLevelDTO createAccessLevelDTO = accessLevelService.insert(expectedAccessLevelDTO);

        assertThat(createAccessLevelDTO,  is(equalTo(expectedAccessLevelDTO)));
    }

    @Test
    void whenAlreadyRegisteredAccessLevelInformedThenAnExceptionShouldBeThrown() {
        // given
        AccessLevelDTO expectedAccessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
        AccessLevel expectedAccessLevel = accessLevelMapper.toModel(expectedAccessLevelDTO);

        // when
        when(accessLevelRepository.findById(VALID_ACCESS_LEVEL_ID)).thenReturn(Optional.of(expectedAccessLevel));

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> accessLevelService.insert(expectedAccessLevelDTO));
    }

    @Test
    void whenValidAccessLeveIdIsGivenThenReturnAAccessLevel() {
        // given
        AccessLevelDTO expectedAccessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
        AccessLevel expectedAccessLevel = accessLevelMapper.toModel(expectedAccessLevelDTO);

        // when
        when(accessLevelRepository.findById(VALID_ACCESS_LEVEL_ID)).thenReturn(Optional.of(expectedAccessLevel));

        // then
        AccessLevelDTO foundAccessLevelDTO = accessLevelService.find(VALID_ACCESS_LEVEL_ID);

        assertThat(foundAccessLevelDTO, is(equalTo(expectedAccessLevelDTO)));
    }

    @Test
    void whenNotRegisteredAccessLevelIdIsGivenThenThrowAnException() {
        // when
        when(accessLevelRepository.findById(INVALID_ACCESS_LEVEL_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> accessLevelService.find(INVALID_ACCESS_LEVEL_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidAccessLevelGivenThenReturnAAccessLevelUpdated() {
        // given
        AccessLevelDTO expectedAccessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
        AccessLevel expectedAccessLevel = accessLevelMapper.toModel(expectedAccessLevelDTO);

        // when
        when(accessLevelRepository.findById(VALID_ACCESS_LEVEL_ID)).thenReturn(Optional.of(expectedAccessLevel));
        when(accessLevelRepository.save(expectedAccessLevel)).thenReturn(expectedAccessLevel);

        // then
        AccessLevelDTO updateAccessLevelDTO = accessLevelService.update(expectedAccessLevelDTO);

        assertThat(updateAccessLevelDTO, is(equalTo(expectedAccessLevelDTO)));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingAccessLevelThenAnExceptionShouldBeThrown() {
        // given
        AccessLevelDTO expectedAccessDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();

        // when
        when(accessLevelRepository.findById(VALID_ACCESS_LEVEL_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> accessLevelService.update(expectedAccessDTO));
    }

    @Test
    void whenListAccessLevelIsCalledThenReturnAListOfAccessLevel() {
        // given
        AccessLevelDTO expectedAccessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
        AccessLevel expectedAccessLevel = accessLevelMapper.toModel(expectedAccessLevelDTO);

        // when
        when(accessLevelRepository.findAll()).thenReturn(Collections.singletonList(expectedAccessLevel));

        // then
        List<AccessLevelDTO> listAccessLevelDTO = accessLevelService.findAll();

        assertThat(listAccessLevelDTO, is(not(empty())));
        assertThat(listAccessLevelDTO.get(0), is(equalTo(expectedAccessLevelDTO)));
    }

    @Test
    void whenListAccessLevelIsCalledThenReturnAnEmptyListOfAccessLevel() {
        // when
        when(accessLevelRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<AccessLevelDTO> listAccessLevelDTO = accessLevelService.findAll();

        assertThat(listAccessLevelDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAAccessLevelShouldBeDeleted() {
        // given
        AccessLevelDTO expectedAccessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
        AccessLevel expectedAccessLevel = accessLevelMapper.toModel(expectedAccessLevelDTO);

        // when
        when(accessLevelRepository.findById(VALID_ACCESS_LEVEL_ID)).thenReturn(Optional.of(expectedAccessLevel));
        doNothing().when(accessLevelRepository).deleteById(VALID_ACCESS_LEVEL_ID);

        // then
        accessLevelService.delete(VALID_ACCESS_LEVEL_ID);

        verify(accessLevelRepository, times(1)).findById(VALID_ACCESS_LEVEL_ID);
        verify(accessLevelRepository, times(1)).deleteById(VALID_ACCESS_LEVEL_ID);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingAccessLevelThenAnExceptionShouldBeThrown() {
        // when
        when(accessLevelRepository.findById(INVALID_ACCESS_LEVEL_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> accessLevelService.delete(INVALID_ACCESS_LEVEL_ID));
    }
}
