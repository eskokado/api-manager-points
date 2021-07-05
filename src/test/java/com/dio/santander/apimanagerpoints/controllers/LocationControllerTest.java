package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.LocationDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.LocationDTO;
import com.dio.santander.apimanagerpoints.services.LocationService;
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
public class LocationControllerTest {
    private static final String LOCATION_API_URL_PATH = "/api/v1/locations";
    private static final Long VALID_LOCATION_ID = 1L;
    private static final Long INVALID_LOCATION_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(locationController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenALocationIsCreated() throws Exception {
        // given
        LocationDTO locationDTO = LocationDTOBuilder.builder().build().toLocationDTO();

        // when
        when(locationService.insert(locationDTO)).thenReturn(locationDTO);

        // then
        mockMvc.perform(post(LOCATION_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(locationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(locationDTO.getDescription()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        LocationDTO locationDTO = LocationDTOBuilder.builder().build().toLocationDTO();
        locationDTO.setDescription(null);

        // then
        mockMvc.perform(post(LOCATION_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(locationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        LocationDTO locationDTO = LocationDTOBuilder.builder().build().toLocationDTO();

        // when
        when(locationService.find(VALID_LOCATION_ID)).thenReturn(locationDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(LOCATION_API_URL_PATH + "/" + VALID_LOCATION_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(locationDTO.getDescription()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(locationService.find(INVALID_LOCATION_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(LOCATION_API_URL_PATH + "/" + INVALID_LOCATION_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithLocationIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        LocationDTO locationDTO = LocationDTOBuilder.builder().build().toLocationDTO();

        // when
        when(locationService.findAll()).thenReturn(Collections.singletonList(locationDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(LOCATION_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(locationDTO.getDescription())));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(locationService).delete(VALID_LOCATION_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(LOCATION_API_URL_PATH + "/" + VALID_LOCATION_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(locationService).delete(INVALID_LOCATION_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(LOCATION_API_URL_PATH + "/" + INVALID_LOCATION_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenALocationIsOkStatusReturned() throws Exception {
        // given
        LocationDTO locationDTO = LocationDTOBuilder.builder().build().toLocationDTO();
        locationDTO.setDescription("Modified Name");
        // when
        when(locationService.update(locationDTO)).thenReturn(locationDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(LOCATION_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(locationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Modified Name")));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        LocationDTO locationDTO = LocationDTOBuilder.builder().build().toLocationDTO();

        // when
        when(locationService.update(locationDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(LOCATION_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(locationDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        LocationDTO locationDTO = LocationDTOBuilder.builder().build().toLocationDTO();
        locationDTO.setDescription(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(LOCATION_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(locationDTO)))
                .andExpect(status().isBadRequest());
    }

}
