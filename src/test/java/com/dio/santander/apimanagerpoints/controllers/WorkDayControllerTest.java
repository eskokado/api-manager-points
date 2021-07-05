package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.WorkDayDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.WorkDayDTO;
import com.dio.santander.apimanagerpoints.services.WorkDayService;

import com.dio.santander.apimanagerpoints.services.exceptions.ObjectAlreadyRegisteredException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
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
public class WorkDayControllerTest {
    private static final String WORK_DAY_API_URL_PATH = "/api/v1/work_days";
    private static final Long VALID_WORK_DAY_ID = 1L;
    private static final Long INVALID_WORK_DAY_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private WorkDayService workDayService;

    @InjectMocks
    private WorkDayController workDayController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(workDayController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAWorkDayIsCreated() throws Exception {
        // given
        WorkDayDTO workDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();

        // when
        when(workDayService.insert(workDayDTO)).thenReturn(workDayDTO);

        // then
        mockMvc.perform(post(WORK_DAY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workDayDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(workDayDTO.getDescription()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        WorkDayDTO workDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
        workDayDTO.setDescription(null);

        // then
        mockMvc.perform(post(WORK_DAY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workDayDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        WorkDayDTO workDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();

        // when
        when(workDayService.find(VALID_WORK_DAY_ID)).thenReturn(workDayDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(WORK_DAY_API_URL_PATH + "/" + VALID_WORK_DAY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(workDayDTO.getDescription()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(workDayService.find(INVALID_WORK_DAY_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(WORK_DAY_API_URL_PATH + "/" + INVALID_WORK_DAY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithWorkDayIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        WorkDayDTO workDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();

        // when
        when(workDayService.findAll()).thenReturn(Collections.singletonList(workDayDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(WORK_DAY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(workDayDTO.getDescription())));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(workDayService).delete(VALID_WORK_DAY_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(WORK_DAY_API_URL_PATH + "/" + VALID_WORK_DAY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(workDayService).delete(INVALID_WORK_DAY_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(WORK_DAY_API_URL_PATH + "/" + INVALID_WORK_DAY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenAWorkDayIsOkStatusReturned() throws Exception {
        // given
        WorkDayDTO workDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
        workDayDTO.setDescription("Modified Name");
        // when
        when(workDayService.update(workDayDTO)).thenReturn(workDayDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(WORK_DAY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workDayDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Modified Name")));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        WorkDayDTO workDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();

        // when
        when(workDayService.update(workDayDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(WORK_DAY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workDayDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        WorkDayDTO workDayDTO = WorkDayDTOBuilder.builder().build().toWorkDayDTO();
        workDayDTO.setDescription(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(WORK_DAY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workDayDTO)))
                .andExpect(status().isBadRequest());
    }

}
