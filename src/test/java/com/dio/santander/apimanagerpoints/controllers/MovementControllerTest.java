package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.MovementDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.MovementDTO;
import com.dio.santander.apimanagerpoints.services.MovementService;
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
public class MovementControllerTest {
    private static final String MOVEMENT_API_URL_PATH = "/api/v1/movements";
    private static final Long VALID_MOVEMENT_ID = 1L;
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_MOVEMENT_ID = 2L;
    private static final Long INVALID_USER_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private MovementService movementService;

    @InjectMocks
    private MovementController movementController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(movementController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAMovementIsCreated() throws Exception {
        // given
        MovementDTO movementDTO = MovementDTOBuilder.builder().build().toMovementDTO();

        // when
        when(movementService.insert(movementDTO)).thenReturn(movementDTO);

        // then
        mockMvc.perform(post(MOVEMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movementDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.occurrence.description").value(movementDTO.getOccurrence().getDescription()))
                .andExpect(jsonPath("$.calendar.description").value(movementDTO.getCalendar().getDescription()))
                .andExpect(jsonPath("$.dateOfIn").value(movementDTO.getDateOfIn()))
                .andExpect(jsonPath("$.dateOfOut").value(movementDTO.getDateOfOut()))
                .andExpect(jsonPath("$.period").value(movementDTO.getPeriod()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        MovementDTO movementDTO = MovementDTOBuilder.builder().build().toMovementDTO();
        movementDTO.setDateOfIn(null);

        // then
        mockMvc.perform(post(MOVEMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movementDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        MovementDTO movementDTO = MovementDTOBuilder.builder().build().toMovementDTO();

        // when
        when(movementService.find(VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(movementDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(MOVEMENT_API_URL_PATH +
                "/pk?movementId=" + VALID_MOVEMENT_ID +
                "&userId=" + VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.occurrence.description").value(movementDTO.getOccurrence().getDescription()))
                .andExpect(jsonPath("$.calendar.description").value(movementDTO.getCalendar().getDescription()))
                .andExpect(jsonPath("$.dateOfIn").value(movementDTO.getDateOfIn()))
                .andExpect(jsonPath("$.dateOfOut").value(movementDTO.getDateOfOut()))
                .andExpect(jsonPath("$.period").value(movementDTO.getPeriod()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(movementService.find(INVALID_MOVEMENT_ID, INVALID_USER_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(MOVEMENT_API_URL_PATH +
                "/pk?movementId=" + INVALID_MOVEMENT_ID +
                "&userId=" + INVALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithMovementIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        MovementDTO movementDTO = MovementDTOBuilder.builder().build().toMovementDTO();

        // when
        when(movementService.findAll()).thenReturn(Collections.singletonList(movementDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(MOVEMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].occurrence.description").value(movementDTO.getOccurrence().getDescription()))
                .andExpect(jsonPath("$[0].calendar.description").value(movementDTO.getCalendar().getDescription()))
                .andExpect(jsonPath("$[0].dateOfIn").value(movementDTO.getDateOfIn()))
                .andExpect(jsonPath("$[0].dateOfOut").value(movementDTO.getDateOfOut()))
                .andExpect(jsonPath("$[0].period").value(movementDTO.getPeriod()));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(movementService).delete(VALID_MOVEMENT_ID, VALID_USER_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(MOVEMENT_API_URL_PATH +
                "?movementId=" + VALID_MOVEMENT_ID +
                "&userId=" + VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(movementService).delete(INVALID_MOVEMENT_ID, INVALID_USER_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(MOVEMENT_API_URL_PATH +
                "?movementId=" + INVALID_MOVEMENT_ID +
                "&userId=" + INVALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenAMovementIsOkStatusReturned() throws Exception {
        // given
        MovementDTO movementDTO = MovementDTOBuilder.builder().build().toMovementDTO();

        // when
        when(movementService.update(movementDTO)).thenReturn(movementDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(MOVEMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movementDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.occurrence.description").value(movementDTO.getOccurrence().getDescription()))
                .andExpect(jsonPath("$.calendar.description").value(movementDTO.getCalendar().getDescription()))
                .andExpect(jsonPath("$.dateOfIn").value(movementDTO.getDateOfIn()))
                .andExpect(jsonPath("$.dateOfOut").value(movementDTO.getDateOfOut()))
                .andExpect(jsonPath("$.period").value(movementDTO.getPeriod()));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        MovementDTO movementDTO = MovementDTOBuilder.builder().build().toMovementDTO();

        // when
        when(movementService.update(movementDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(MOVEMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movementDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        MovementDTO movementDTO = MovementDTOBuilder.builder().build().toMovementDTO();
        movementDTO.setDateOfIn(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(MOVEMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movementDTO)))
                .andExpect(status().isBadRequest());
    }

}
