package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.AccessLevelDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.AccessLevelDTO;
import com.dio.santander.apimanagerpoints.services.AccessLevelService;
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
public class AccessLevelControllerTest {
    private static final String ACCESS_LEVEL_API_URL_PATH = "/api/v1/access_levels";
    private static final Long VALID_ACCESS_LEVEL_ID = 1L;
    private static final Long INVALID_ACCESS_LEVEL_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private AccessLevelService accessLevelService;

    @InjectMocks
    private AccessLevelController accessLevelController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accessLevelController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAAccessLevelIsCreated() throws Exception {
        // given
        AccessLevelDTO accessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();

        // when
        when(accessLevelService.insert(accessLevelDTO)).thenReturn(accessLevelDTO);

        // then
        mockMvc.perform(post(ACCESS_LEVEL_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accessLevelDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(accessLevelDTO.getDescription()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        AccessLevelDTO accessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
        accessLevelDTO.setDescription(null);

        // then
        mockMvc.perform(post(ACCESS_LEVEL_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accessLevelDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        AccessLevelDTO accessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();

        // when
        when(accessLevelService.find(VALID_ACCESS_LEVEL_ID)).thenReturn(accessLevelDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(ACCESS_LEVEL_API_URL_PATH + "/" + VALID_ACCESS_LEVEL_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(accessLevelDTO.getDescription()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(accessLevelService.find(INVALID_ACCESS_LEVEL_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(ACCESS_LEVEL_API_URL_PATH + "/" + INVALID_ACCESS_LEVEL_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithAccessLevelIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        AccessLevelDTO accessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();

        // when
        when(accessLevelService.findAll()).thenReturn(Collections.singletonList(accessLevelDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(ACCESS_LEVEL_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(accessLevelDTO.getDescription())));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(accessLevelService).delete(VALID_ACCESS_LEVEL_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(ACCESS_LEVEL_API_URL_PATH + "/" + VALID_ACCESS_LEVEL_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(accessLevelService).delete(INVALID_ACCESS_LEVEL_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(ACCESS_LEVEL_API_URL_PATH + "/" + INVALID_ACCESS_LEVEL_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenAAccessLevelIsOkStatusReturned() throws Exception {
        // given
        AccessLevelDTO accessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
        accessLevelDTO.setDescription("Modified Name");
        // when
        when(accessLevelService.update(accessLevelDTO)).thenReturn(accessLevelDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(ACCESS_LEVEL_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accessLevelDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Modified Name")));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        AccessLevelDTO accessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();

        // when
        when(accessLevelService.update(accessLevelDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(ACCESS_LEVEL_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accessLevelDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        AccessLevelDTO accessLevelDTO = AccessLevelDTOBuilder.builder().build().toAccessLevelDTO();
        accessLevelDTO.setDescription(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(ACCESS_LEVEL_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accessLevelDTO)))
                .andExpect(status().isBadRequest());
    }

}
