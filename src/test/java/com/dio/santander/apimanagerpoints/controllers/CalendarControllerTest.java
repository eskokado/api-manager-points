package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.CalendarDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.CalendarDTO;
import com.dio.santander.apimanagerpoints.services.CalendarService;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.dio.santander.apimanagerpoints.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CalendarControllerTest {
    private static final String CALENDAR_API_URL_PATH = "/api/v1/calendars";
    private static final Long VALID_CALENDAR_ID = 1L;
    private static final Long INVALID_CALENDAR_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private CalendarService calendarService;

    @InjectMocks
    private CalendarController calendarController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(calendarController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenACalendarIsCreated() throws Exception {
        // given
        CalendarDTO calendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();

        // when
        when(calendarService.insert(calendarDTO)).thenReturn(calendarDTO);

        // then
        mockMvc.perform(post(CALENDAR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(calendarDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(calendarDTO.getDescription()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        CalendarDTO calendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();
        calendarDTO.setDescription(null);

        // then
        mockMvc.perform(post(CALENDAR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(calendarDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        CalendarDTO calendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();

        // when
        when(calendarService.find(VALID_CALENDAR_ID)).thenReturn(calendarDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(CALENDAR_API_URL_PATH + "/" + VALID_CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(calendarDTO.getDescription()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(calendarService.find(INVALID_CALENDAR_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(CALENDAR_API_URL_PATH + "/" + INVALID_CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithCalendarIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        CalendarDTO calendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();

        // when
        when(calendarService.findAll()).thenReturn(Collections.singletonList(calendarDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(CALENDAR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(calendarDTO.getDescription())));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(calendarService).delete(VALID_CALENDAR_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(CALENDAR_API_URL_PATH + "/" + VALID_CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(calendarService).delete(INVALID_CALENDAR_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(CALENDAR_API_URL_PATH + "/" + INVALID_CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenACalendarIsOkStatusReturned() throws Exception {
        // given
        CalendarDTO calendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();
        calendarDTO.setDescription("Modified Name");
        // when
        when(calendarService.update(calendarDTO)).thenReturn(calendarDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(CALENDAR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(calendarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Modified Name")));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        CalendarDTO calendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();

        // when
        when(calendarService.update(calendarDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(CALENDAR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(calendarDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        CalendarDTO calendarDTO = CalendarDTOBuilder.builder().build().toCalendarDTO();
        calendarDTO.setDescription(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(CALENDAR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(calendarDTO)))
                .andExpect(status().isBadRequest());
    }

}
