package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.CalendarDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.CalendarDTO;
import com.dio.santander.apimanagerpoints.mappers.CalendarMapper;
import com.dio.santander.apimanagerpoints.models.Calendar;
import com.dio.santander.apimanagerpoints.repositories.CalendarRepository;
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
public class CalendarServiceTest {
    private static final Long VALID_CALENDAR_ID = 1L;
    private static final Long INVALID_CALENDAR_ID = 2L;

    @Mock
    private CalendarRepository calendarRepository;

    private CalendarMapper calendarMapper = CalendarMapper.INSTANCE;

    @InjectMocks
    private CalendarService calendarService;

    @Test
    void whenCalendarInformedIsNotExistingThenItShouldBeCreated() {
        // given
        CalendarDTO expectedCalendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();
        Calendar expectedCalendar = calendarMapper.toModel(expectedCalendarDTO);

        // when
        when(calendarRepository.findById(VALID_CALENDAR_ID)).thenReturn(Optional.empty());
        when(calendarRepository.save(expectedCalendar)).thenReturn(expectedCalendar);

        // then
        CalendarDTO createCalendarDTO = calendarService.insert(expectedCalendarDTO);

        assertThat(createCalendarDTO, is(equalTo(expectedCalendarDTO)));
    }

    @Test
    void whenAlreadyRegisteredCalendarInformedThenAnExceptionShouldBeThrown() {
        // given
        CalendarDTO expectedCalendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();
        Calendar expectedCalendar = calendarMapper.toModel(expectedCalendarDTO);

        // when
        when(calendarRepository.findById(VALID_CALENDAR_ID)).thenReturn(Optional.of(expectedCalendar));

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> calendarService.insert(expectedCalendarDTO));
    }

    @Test
    void whenValidCalendarIdIsGivenThenReturnAnCalendar() {
        // given
        CalendarDTO expectedCalendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();
        Calendar expectedCalendar = calendarMapper.toModel(expectedCalendarDTO);

        // when
        when(calendarRepository.findById(VALID_CALENDAR_ID)).thenReturn(Optional.of(expectedCalendar));

        // then
        CalendarDTO foundCalendarDTO = calendarService.find(VALID_CALENDAR_ID);

        assertThat(foundCalendarDTO, is(equalTo(expectedCalendarDTO)));
    }

    @Test
    void whenNotRegisteredCalendarIdGivenThenThrowAnException() {
        // when
        when(calendarRepository.findById(INVALID_CALENDAR_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> calendarService.find(INVALID_CALENDAR_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidCalendarGivenThenReturnACalendarUpdated() {
        // given
        CalendarDTO expectedCalendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();
        Calendar expectedCalendar = calendarMapper.toModel(expectedCalendarDTO);

        // when
        when(calendarRepository.findById(VALID_CALENDAR_ID)).thenReturn(Optional.of(expectedCalendar));
        when(calendarRepository.save(expectedCalendar)).thenReturn(expectedCalendar);

        // then
        CalendarDTO updateCalendarDTO = calendarService.update(expectedCalendarDTO);

        assertThat(updateCalendarDTO, is(equalTo(expectedCalendarDTO)));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingCalendarThenAnExceptionShouldBeThrown() {
        // given
        CalendarDTO expectedCalendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();

        // when
        when(calendarRepository.findById(VALID_CALENDAR_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> calendarService.update(expectedCalendarDTO));
    }

    @Test
    void whenListCalendarIsCalledThenReturnAListOfCalendar() {
        // given
        CalendarDTO expectedCalendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();
        Calendar expectedCalendar = calendarMapper.toModel(expectedCalendarDTO);

        // when
        when(calendarRepository.findAll()).thenReturn(Collections.singletonList(expectedCalendar));

        // then
        List<CalendarDTO> listCalendarDTO = calendarService.findAll();

        assertThat(listCalendarDTO, is(not(empty())));
        assertThat(listCalendarDTO.get(0), is(equalTo(expectedCalendarDTO)));
    }

    @Test
    void whenListCalendarIsCalledThenReturnAnEmptyListOfCalendar() {
        // when
        when(calendarRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<CalendarDTO> listCalendarDTO = calendarService.findAll();

        assertThat(listCalendarDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenACalendarShouldBeDeleted() {
        // given
        CalendarDTO expectedCalendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();
        Calendar expectedCalendar = calendarMapper.toModel(expectedCalendarDTO);

        //  when
        when(calendarRepository.findById(VALID_CALENDAR_ID)).thenReturn(Optional.of(expectedCalendar));
        doNothing().when(calendarRepository).deleteById(VALID_CALENDAR_ID);

        // then
        calendarService.delete(VALID_CALENDAR_ID);

        verify(calendarRepository, times(1)).findById(VALID_CALENDAR_ID);
        verify(calendarRepository, times(1)).deleteById(VALID_CALENDAR_ID);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingCalendarThenanExceptionShouldBeThrown() {
        // when
        when(calendarRepository.findById(INVALID_CALENDAR_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> calendarService.delete(INVALID_CALENDAR_ID));
    }
}
