package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.DateTypeDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.DateTypeDTO;
import com.dio.santander.apimanagerpoints.services.DateTypeService;
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
public class DateTypeControllerTest {
    private static final String DATE_TYPE_API_URL_PATH = "/api/v1/date_types";
    private static final Long VALID_DATE_TYPE_ID = 1L;
    private static final Long INVALID_DATE_TYPE_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private DateTypeService dateTypeService;

    @InjectMocks
    private DateTypeController dateTypeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dateTypeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenADateTypeIsCreated() throws Exception {
        // given
        DateTypeDTO dateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();

        // when
        when(dateTypeService.insert(dateTypeDTO)).thenReturn(dateTypeDTO);

        // then
        mockMvc.perform(post(DATE_TYPE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dateTypeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(dateTypeDTO.getDescription()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        DateTypeDTO dateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();
        dateTypeDTO.setDescription(null);

        // then
        mockMvc.perform(post(DATE_TYPE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dateTypeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        DateTypeDTO dateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();

        // when
        when(dateTypeService.find(VALID_DATE_TYPE_ID)).thenReturn(dateTypeDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(DATE_TYPE_API_URL_PATH + "/" + VALID_DATE_TYPE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(dateTypeDTO.getDescription()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(dateTypeService.find(INVALID_DATE_TYPE_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(DATE_TYPE_API_URL_PATH + "/" + INVALID_DATE_TYPE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithDateTypeIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        DateTypeDTO dateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();

        // when
        when(dateTypeService.findAll()).thenReturn(Collections.singletonList(dateTypeDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(DATE_TYPE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(dateTypeDTO.getDescription())));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(dateTypeService).delete(VALID_DATE_TYPE_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(DATE_TYPE_API_URL_PATH + "/" + VALID_DATE_TYPE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(dateTypeService).delete(INVALID_DATE_TYPE_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(DATE_TYPE_API_URL_PATH + "/" + INVALID_DATE_TYPE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenADateTypeIsOkStatusReturned() throws Exception {
        // given
        DateTypeDTO dateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();
        dateTypeDTO.setDescription("Modified Name");
        // when
        when(dateTypeService.update(dateTypeDTO)).thenReturn(dateTypeDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(DATE_TYPE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dateTypeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Modified Name")));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        DateTypeDTO dateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();

        // when
        when(dateTypeService.update(dateTypeDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(DATE_TYPE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dateTypeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        DateTypeDTO dateTypeDTO = DateTypeDTOBuilder.builder().build().toDateTypeDTO();
        dateTypeDTO.setDescription(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(DATE_TYPE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dateTypeDTO)))
                .andExpect(status().isBadRequest());
    }

}
