package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.OccurrenceDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.OccurrenceDTO;
import com.dio.santander.apimanagerpoints.mappers.OccurrenceMapper;
import com.dio.santander.apimanagerpoints.models.Occurrence;
import com.dio.santander.apimanagerpoints.repositories.OccurrenceRepository;
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
public class OccurrenceServiceTest {
    private static final Long VALID_OCCURRENCE_ID = 1L;
    private static final Long INVALID_OCCURRENCE_ID = 2L;

    @Mock
    private OccurrenceRepository occurrenceRepository;

    private OccurrenceMapper occurrenceMapper = OccurrenceMapper.INSTANCE;

    @InjectMocks
    private OccurrenceService occurrenceService;

    @Test
    void whenOccurrenceInformedIsNotExistingThenItShouldBeCreated() {
        // given
        OccurrenceDTO expectedOccurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
        Occurrence expectedOccurrence = occurrenceMapper.toModel(expectedOccurrenceDTO);

        // when
        when(occurrenceRepository.findById(VALID_OCCURRENCE_ID)).thenReturn(Optional.empty());
        when(occurrenceRepository.save(expectedOccurrence)).thenReturn(expectedOccurrence);

        // then
        OccurrenceDTO createOccurrenceDTO = occurrenceService.insert(expectedOccurrenceDTO);

        assertThat(createOccurrenceDTO, is(equalTo(expectedOccurrenceDTO)));
    }

    @Test
    void whenAlreadyRegisteredOccurrenceInformedThenAnExceptionShouldBeThrown() {
        // given
        OccurrenceDTO expectedOccurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
        Occurrence expectedOccurrence = occurrenceMapper.toModel(expectedOccurrenceDTO);

        // when
        when(occurrenceRepository.findById(VALID_OCCURRENCE_ID)).thenReturn(Optional.of(expectedOccurrence));

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> occurrenceService.insert(expectedOccurrenceDTO));
    }

    @Test
    void whenValidOccurrenceIdIsGivenThenReturnAnOccurrence() {
        // given
        OccurrenceDTO expectedOccurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
        Occurrence expectedOccurrence = occurrenceMapper.toModel(expectedOccurrenceDTO);

        // when
        when(occurrenceRepository.findById(VALID_OCCURRENCE_ID)).thenReturn(Optional.of(expectedOccurrence));

        // then
        OccurrenceDTO foundOccurrenceDTO = occurrenceService.find(VALID_OCCURRENCE_ID);

        assertThat(foundOccurrenceDTO, is(equalTo(expectedOccurrenceDTO)));
    }

    @Test
    void whenNotRegisteredOccurrenceIdGivenThenThrowAnException() {
        // when
        when(occurrenceRepository.findById(INVALID_OCCURRENCE_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> occurrenceService.find(INVALID_OCCURRENCE_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidOccurrenceGivenThenReturnAOccurrenceUpdated() {
        // given
        OccurrenceDTO expectedOccurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
        Occurrence expectedOccurrence = occurrenceMapper.toModel(expectedOccurrenceDTO);

        // when
        when(occurrenceRepository.findById(VALID_OCCURRENCE_ID)).thenReturn(Optional.of(expectedOccurrence));
        when(occurrenceRepository.save(expectedOccurrence)).thenReturn(expectedOccurrence);

        // then
        OccurrenceDTO updateOccurrenceDTO = occurrenceService.update(expectedOccurrenceDTO);

        assertThat(updateOccurrenceDTO, is(equalTo(expectedOccurrenceDTO)));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingOccurrenceThenAnExceptionShouldBeThrown() {
        // given
        OccurrenceDTO expectedOccurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();

        // when
        when(occurrenceRepository.findById(VALID_OCCURRENCE_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> occurrenceService.update(expectedOccurrenceDTO));
    }

    @Test
    void whenListOccurrenceIsCalledThenReturnAListOfOccurrence() {
        // given
        OccurrenceDTO expectedOccurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
        Occurrence expectedOccurrence = occurrenceMapper.toModel(expectedOccurrenceDTO);

        // when
        when(occurrenceRepository.findAll()).thenReturn(Collections.singletonList(expectedOccurrence));

        // then
        List<OccurrenceDTO> listOccurrenceDTO = occurrenceService.findAll();

        assertThat(listOccurrenceDTO, is(not(empty())));
        assertThat(listOccurrenceDTO.get(0), is(equalTo(expectedOccurrenceDTO)));
    }

    @Test
    void whenListOccurrenceIsCalledThenReturnAnEmptyListOfOccurrence() {
        // when
        when(occurrenceRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<OccurrenceDTO> listOccurrenceDTO = occurrenceService.findAll();

        assertThat(listOccurrenceDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAOccurrenceShouldBeDeleted() {
        // given
        OccurrenceDTO expectedOccurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
        Occurrence expectedOccurrence = occurrenceMapper.toModel(expectedOccurrenceDTO);

        //  when
        when(occurrenceRepository.findById(VALID_OCCURRENCE_ID)).thenReturn(Optional.of(expectedOccurrence));
        doNothing().when(occurrenceRepository).deleteById(VALID_OCCURRENCE_ID);

        // then
        occurrenceService.delete(VALID_OCCURRENCE_ID);

        verify(occurrenceRepository, times(1)).findById(VALID_OCCURRENCE_ID);
        verify(occurrenceRepository, times(1)).deleteById(VALID_OCCURRENCE_ID);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingOccurrenceThenanExceptionShouldBeThrown() {
        // when
        when(occurrenceRepository.findById(INVALID_OCCURRENCE_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> occurrenceService.delete(INVALID_OCCURRENCE_ID));
    }
}
