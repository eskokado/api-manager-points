package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.WorkDayDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.WorkDayDTO;
import com.dio.santander.apimanagerpoints.mappers.WorkDayMapper;
import com.dio.santander.apimanagerpoints.models.WorkDay;
import com.dio.santander.apimanagerpoints.repositories.WorkDayRepository;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectAlreadyRegisteredException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectNotFoundException;
import lombok.With;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkDayServiceTest {
    private static final Long VALID_WORK_DAY_ID = 1L;
    private static final Long INVALID_WORK_DAY_ID = 2L;

    @Mock
    private WorkDayRepository workDayRepository;

    private WorkDayMapper workDayMapper = WorkDayMapper.INSTANCE;

    @InjectMocks
    private WorkDayService workDayService;

    @Test
    void whenWorkDayInformedIsNotExistingThenItShouldBeCreated() {
        // given
        WorkDayDTO expectedWorkDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
        WorkDay expectedSavedWorkDay = workDayMapper.toModel(expectedWorkDayDTO);

        // when
        when(workDayRepository.findById(VALID_WORK_DAY_ID)).thenReturn(Optional.empty());
        when(workDayRepository.save(Mockito.any())).thenReturn(expectedSavedWorkDay);

        //then
        WorkDayDTO createWorkDayDTO = workDayService.insert(expectedWorkDayDTO);

        assertThat(createWorkDayDTO.getId(), is(equalTo(expectedWorkDayDTO.getId())));
        assertThat(createWorkDayDTO.getDescription(), is(equalTo(expectedWorkDayDTO.getDescription())));
    }

    @Test
    void whenAlreadyRegisteredWorkDayInformedThenAnExceptionShouldBeThrown() {
        // given
        WorkDayDTO expectedWorkDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
        WorkDay duplicateWorkDay = workDayMapper.toModel(expectedWorkDayDTO);

        // when
        when(workDayRepository.findById(VALID_WORK_DAY_ID)).thenReturn(Optional.of(duplicateWorkDay));

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> workDayService.insert(expectedWorkDayDTO));
    }

    @Test
    void whenValidWorkDayIdIsGivenThenReturnAWorkDay() {
        // given
        WorkDayDTO expectedFoundWorkDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
        WorkDay expectedFoundWorkDay = workDayMapper.toModel(expectedFoundWorkDayDTO);

        // when
        when(workDayRepository.findById(VALID_WORK_DAY_ID)).thenReturn(Optional.of(expectedFoundWorkDay));

        // then
        WorkDayDTO foundWorkDayDTO = workDayService.find(VALID_WORK_DAY_ID);

        assertThat(foundWorkDayDTO, is(equalTo(expectedFoundWorkDayDTO)));
    }

    @Test
    void whenNotRegisteredWorkDayIdIsGivenThenThrowAnException() {
        // when
        when(workDayRepository.findById(INVALID_WORK_DAY_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> workDayService.find(INVALID_WORK_DAY_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidWorkDayUpdateGivenThenReturnAWorkDayUpdated() {
        // given
        WorkDayDTO expectedUpdatedWorkDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
        WorkDay expectedUpdatedWorkDay = workDayMapper.toModel(expectedUpdatedWorkDayDTO);

        // when
        when(workDayRepository.findById(VALID_WORK_DAY_ID)).thenReturn(Optional.of(expectedUpdatedWorkDay));
        when(workDayRepository.save(expectedUpdatedWorkDay)).thenReturn(expectedUpdatedWorkDay);

        // then
        WorkDayDTO updateWorkDayDTO = workDayService.update(expectedUpdatedWorkDayDTO);

        assertThat(updateWorkDayDTO.getId(), is(equalTo(expectedUpdatedWorkDayDTO.getId())));
        assertThat(updateWorkDayDTO.getDescription(), is(equalTo(expectedUpdatedWorkDayDTO.getDescription())));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingWorkDayThenAnExceptionShouldBeThrown() {
        // given
        WorkDayDTO expectedFoundWorkDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();

        // when
        when(workDayRepository.findById(VALID_WORK_DAY_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> workDayService.update(expectedFoundWorkDayDTO));
    }

    @Test
    void whenListWorkDayIsCalledThenReturnAListOfWorkDay() {
        // given
        WorkDayDTO expectedWorkDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
        WorkDay expectedWorkDay = workDayMapper.toModel(expectedWorkDayDTO);

        // when
        when(workDayRepository.findAll()).thenReturn(Collections.singletonList(expectedWorkDay));

        // then
        List<WorkDayDTO> foundListWorkDaysDTO = workDayService.findAll();

        assertThat(foundListWorkDaysDTO, is(not(empty())));
        assertThat(foundListWorkDaysDTO.get(0), is(equalTo(expectedWorkDayDTO)));
    }

    @Test
    void whenListWorkDayIsCalledThenReturnAnEmptyListOfWorkDay() {
        // when
        when(workDayRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<WorkDayDTO> foundListWorkDaysDTO = workDayService.findAll();

        assertThat(foundListWorkDaysDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAWorkDayShouldBeDeleted() {
        // given
        WorkDayDTO expectedDeletedWorkDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
        WorkDay expectedDeletedWorkDay = workDayMapper.toModel(expectedDeletedWorkDayDTO);

        // when
        when(workDayRepository.findById(VALID_WORK_DAY_ID)).thenReturn(Optional.of(expectedDeletedWorkDay));
        doNothing().when(workDayRepository).deleteById(VALID_WORK_DAY_ID);

        // then
        workDayService.delete(VALID_WORK_DAY_ID);

        verify(workDayRepository, times(1)).findById(VALID_WORK_DAY_ID);
        verify(workDayRepository, times(1)).deleteById(VALID_WORK_DAY_ID);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingWorkDayThenAnExceptionShouldBeThrown() {
        // when
        when(workDayRepository.findById(INVALID_WORK_DAY_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> workDayService.delete(INVALID_WORK_DAY_ID));
    }
}
