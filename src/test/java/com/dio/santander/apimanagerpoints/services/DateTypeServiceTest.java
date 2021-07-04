package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.DateTypeDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.DateTypeDTO;
import com.dio.santander.apimanagerpoints.dtos.WorkDayDTO;
import com.dio.santander.apimanagerpoints.mappers.DateTypeMapper;
import com.dio.santander.apimanagerpoints.models.DateType;
import com.dio.santander.apimanagerpoints.repositories.DateTypeRepository;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectAlreadyRegisteredException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DateTypeServiceTest {
    private static final Long VALID_DATE_TYPE_ID = 1L;
    private static final Long INVALID_DATE_TYPE_ID = 2L;

    @Mock
    private DateTypeRepository dateTypeRepository;

    private DateTypeMapper dateTypeMapper = DateTypeMapper.INSTANCE;

    @InjectMocks
    private DateTypeService dateTypeService;

    @Test
    void whenDateTypeInformedIsNotExistingThenItShouldBeCreated() {
        // given
        DateTypeDTO expectedDateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();
        DateType expectedDateType = dateTypeMapper.toModel(expectedDateTypeDTO);
        
        // when
        when(dateTypeRepository.findById(VALID_DATE_TYPE_ID)).thenReturn(Optional.empty());
        when(dateTypeRepository.save(expectedDateType)).thenReturn(expectedDateType);

        // then
        DateTypeDTO createDateType = dateTypeService.insert(expectedDateTypeDTO);

        assertThat(createDateType, is(equalTo(expectedDateTypeDTO)));
    }

    @Test
    void whenAlreadyRegisteredDateTypeInformedThenAnExceptionShouldBeThrown() {
        // given
        DateTypeDTO expectedDateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();
        DateType expectedDateType = dateTypeMapper.toModel(expectedDateTypeDTO);

        // when
        when(dateTypeRepository.findById(VALID_DATE_TYPE_ID)).thenReturn(Optional.of(expectedDateType));

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> dateTypeService.insert(expectedDateTypeDTO));
    }

    @Test
    void whenValidDateTypeIdIsGivenThenReturnADateType() {
        // given
        DateTypeDTO expectedFoundDateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();
        DateType expectedFoundDateType = dateTypeMapper.toModel(expectedFoundDateTypeDTO);

        // when
        when(dateTypeRepository.findById(VALID_DATE_TYPE_ID)).thenReturn(Optional.of(expectedFoundDateType));

        // then
        DateTypeDTO foundDateTypeDTO = dateTypeService.find(VALID_DATE_TYPE_ID);

        assertThat(foundDateTypeDTO, is(equalTo(expectedFoundDateTypeDTO)));
    }

    @Test
    void whenNotRegisteredDateTypeIdIsGivenThenThrowAnException() {
        // when
        when(dateTypeRepository.findById(INVALID_DATE_TYPE_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> dateTypeService.find(INVALID_DATE_TYPE_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidDateTypeUpdateGivenThenReturnADateTypeUpdated() {
        // given
        DateTypeDTO expectedUpdatedDateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();
        DateType expectedUpdateDateType = dateTypeMapper.toModel(expectedUpdatedDateTypeDTO);

        // when
        when(dateTypeRepository.findById(VALID_DATE_TYPE_ID)).thenReturn(Optional.of(expectedUpdateDateType));
        when(dateTypeRepository.save(expectedUpdateDateType)).thenReturn(expectedUpdateDateType);

        // then
        DateTypeDTO updateDateTypeDTO = dateTypeService.update(expectedUpdatedDateTypeDTO);

        assertThat(updateDateTypeDTO, is(equalTo(expectedUpdatedDateTypeDTO)));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingDateTypeThenAnExceptionShouldBeThrown() {
        // given
        DateTypeDTO expectedFoundDateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();

        // when
        when(dateTypeRepository.findById(VALID_DATE_TYPE_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> dateTypeService.update(expectedFoundDateTypeDTO));
    }

    @Test
    void whenListDateTypeIsCalledThenReturnAListOfDateType() {
        // given
        DateTypeDTO expectedDateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();
        DateType expectedDateType = dateTypeMapper.toModel(expectedDateTypeDTO);

        // when
        when(dateTypeRepository.findAll()).thenReturn(Collections.singletonList(expectedDateType));

        // then
        List<DateTypeDTO> listDateTypeDTO = dateTypeService.findAll();

        assertThat(listDateTypeDTO, is(not(empty())));
        assertThat(listDateTypeDTO.get(0), is(equalTo(expectedDateTypeDTO)));
    }

    @Test
    void whenListDateTypeIsCalledThenReturnAnEmptyListOfDateType() {
        // when
        when(dateTypeRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<DateTypeDTO> listDateTypeDTO = dateTypeService.findAll();

        assertThat(listDateTypeDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenADateTypeShouldBeDeleted() {
        // given
        DateTypeDTO expectedDeletedDateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();
        DateType expectedDeletedDateType = dateTypeMapper.toModel(expectedDeletedDateTypeDTO);

        // when
        when(dateTypeRepository.findById(VALID_DATE_TYPE_ID)).thenReturn(Optional.of(expectedDeletedDateType));
        doNothing().when(dateTypeRepository).deleteById(VALID_DATE_TYPE_ID);

        // then
        dateTypeService.delete(VALID_DATE_TYPE_ID);

        verify(dateTypeRepository, times(1)).findById(VALID_DATE_TYPE_ID);
        verify(dateTypeRepository, times(1)).deleteById(VALID_DATE_TYPE_ID);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingDateTypeThenAnExceptionShouldBeThrown() {
        // when
        when(dateTypeRepository.findById(INVALID_DATE_TYPE_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> dateTypeService.delete(INVALID_DATE_TYPE_ID));
    }
}
