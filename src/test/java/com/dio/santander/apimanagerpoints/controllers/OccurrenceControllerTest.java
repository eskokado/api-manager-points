package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.OccurrenceDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.OccurrenceDTO;
import com.dio.santander.apimanagerpoints.services.OccurrenceService;
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
public class OccurrenceControllerTest {
    private static final String OCCURRENCE_API_URL_PATH = "/api/v1/occurrences";
    private static final Long VALID_OCCURRENCE_ID = 1L;
    private static final Long INVALID_OCCURRENCE_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private OccurrenceService occurrenceService;

    @InjectMocks
    private OccurrenceController occurrenceController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(occurrenceController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAOccurrenceIsCreated() throws Exception {
        // given
        OccurrenceDTO occurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();

        // when
        when(occurrenceService.insert(occurrenceDTO)).thenReturn(occurrenceDTO);

        // then
        mockMvc.perform(post(OCCURRENCE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(occurrenceDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(occurrenceDTO.getDescription()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        OccurrenceDTO occurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
        occurrenceDTO.setDescription(null);

        // then
        mockMvc.perform(post(OCCURRENCE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(occurrenceDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        OccurrenceDTO occurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();

        // when
        when(occurrenceService.find(VALID_OCCURRENCE_ID)).thenReturn(occurrenceDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(OCCURRENCE_API_URL_PATH + "/" + VALID_OCCURRENCE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(occurrenceDTO.getDescription()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(occurrenceService.find(INVALID_OCCURRENCE_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(OCCURRENCE_API_URL_PATH + "/" + INVALID_OCCURRENCE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithOccurrenceIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        OccurrenceDTO occurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();

        // when
        when(occurrenceService.findAll()).thenReturn(Collections.singletonList(occurrenceDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(OCCURRENCE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(occurrenceDTO.getDescription())));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(occurrenceService).delete(VALID_OCCURRENCE_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(OCCURRENCE_API_URL_PATH + "/" + VALID_OCCURRENCE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(occurrenceService).delete(INVALID_OCCURRENCE_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(OCCURRENCE_API_URL_PATH + "/" + INVALID_OCCURRENCE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenAOccurrenceIsOkStatusReturned() throws Exception {
        // given
        OccurrenceDTO occurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
        occurrenceDTO.setDescription("Modified Name");
        // when
        when(occurrenceService.update(occurrenceDTO)).thenReturn(occurrenceDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(OCCURRENCE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(occurrenceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Modified Name")));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        OccurrenceDTO occurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();

        // when
        when(occurrenceService.update(occurrenceDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(OCCURRENCE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(occurrenceDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        OccurrenceDTO occurrenceDTO = OccurrenceDTOBuilder.builder().build().toOccurrenceDTO();
        occurrenceDTO.setDescription(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(OCCURRENCE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(occurrenceDTO)))
                .andExpect(status().isBadRequest());
    }

}
