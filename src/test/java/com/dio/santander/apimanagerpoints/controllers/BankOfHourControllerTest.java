package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.BankOfHourDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.BankOfHourDTO;
import com.dio.santander.apimanagerpoints.services.BankOfHourService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BankOfHourControllerTest {
    private static final String BANK_OF_HOUR_API_URL_PATH = "/api/v1/bank_of_hours";
    private static final Long VALID_BANK_OF_HOUR_ID = 1L;
    private static final Long VALID_MOVEMENT_ID = 1L;
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_BANK_OF_HOUR_ID = 2L;
    private static final Long INVALID_MOVEMENT_ID = 2L;
    private static final Long INVALID_USER_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private BankOfHourService bankOfHourService;

    @InjectMocks
    private BankOfHourController bankOfHourController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bankOfHourController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenABankOfHourIsCreated() throws Exception {
        // given
        BankOfHourDTO bankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();

        // when
        when(bankOfHourService.insert(bankOfHourDTO)).thenReturn(bankOfHourDTO);

        // then
        mockMvc.perform(post(BANK_OF_HOUR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bankOfHourDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dateWorked").value(bankOfHourDTO.getDateWorked()))
                .andExpect(jsonPath("$.userCategory.description").value(bankOfHourDTO.getUserCategory().getDescription()))
                .andExpect(jsonPath("$.amountOfHour").value(bankOfHourDTO.getAmountOfHour()))
                .andExpect(jsonPath("$.balanceOfHour").value(bankOfHourDTO.getBalanceOfHour()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        BankOfHourDTO bankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();
        bankOfHourDTO.setDateWorked(null);

        // then
        mockMvc.perform(post(BANK_OF_HOUR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bankOfHourDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        BankOfHourDTO bankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();

        // when
        when(bankOfHourService.find(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID))
                .thenReturn(bankOfHourDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BANK_OF_HOUR_API_URL_PATH +
                "/pk?bankOfHourId=" + VALID_BANK_OF_HOUR_ID +
                "&movementId=" + VALID_MOVEMENT_ID +
                "&userId=" + VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateWorked").value(bankOfHourDTO.getDateWorked()))
                .andExpect(jsonPath("$.userCategory.description").value(bankOfHourDTO.getUserCategory().getDescription()))
                .andExpect(jsonPath("$.amountOfHour").value(bankOfHourDTO.getAmountOfHour()))
                .andExpect(jsonPath("$.balanceOfHour").value(bankOfHourDTO.getBalanceOfHour()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(bankOfHourService
                .find(INVALID_BANK_OF_HOUR_ID, INVALID_MOVEMENT_ID, INVALID_USER_ID))
                .thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BANK_OF_HOUR_API_URL_PATH +
                "/pk?bankOfHourId=" + INVALID_BANK_OF_HOUR_ID +
                "&movementId=" + INVALID_MOVEMENT_ID +
                "&userId=" + INVALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithBankOfHourIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        BankOfHourDTO bankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();

        // when
        when(bankOfHourService.findAll()).thenReturn(Collections.singletonList(bankOfHourDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BANK_OF_HOUR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dateWorked").value(bankOfHourDTO.getDateWorked()))
                .andExpect(jsonPath("$[0].userCategory.description").value(bankOfHourDTO.getUserCategory().getDescription()))
                .andExpect(jsonPath("$[0].amountOfHour").value(bankOfHourDTO.getAmountOfHour()))
                .andExpect(jsonPath("$[0].balanceOfHour").value(bankOfHourDTO.getBalanceOfHour()));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(bankOfHourService).delete(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(BANK_OF_HOUR_API_URL_PATH +
                "?bankOfHourId=" + VALID_BANK_OF_HOUR_ID +
                "&movementId=" + VALID_MOVEMENT_ID +
                "&userId=" + VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class)
                .when(bankOfHourService)
                .delete(INVALID_BANK_OF_HOUR_ID, INVALID_MOVEMENT_ID, INVALID_USER_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(BANK_OF_HOUR_API_URL_PATH +
                "?bankOfHourId=" + INVALID_BANK_OF_HOUR_ID +
                "&movementId=" + INVALID_MOVEMENT_ID +
                "&userId=" + INVALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenABankOfHourIsOkStatusReturned() throws Exception {
        // given
        BankOfHourDTO bankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();

        // when
        when(bankOfHourService.update(bankOfHourDTO)).thenReturn(bankOfHourDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(BANK_OF_HOUR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bankOfHourDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateWorked").value(bankOfHourDTO.getDateWorked()))
                .andExpect(jsonPath("$.userCategory.description").value(bankOfHourDTO.getUserCategory().getDescription()))
                .andExpect(jsonPath("$.amountOfHour").value(bankOfHourDTO.getAmountOfHour()))
                .andExpect(jsonPath("$.balanceOfHour").value(bankOfHourDTO.getBalanceOfHour()));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        BankOfHourDTO bankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();

        // when
        when(bankOfHourService.update(bankOfHourDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(BANK_OF_HOUR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bankOfHourDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        BankOfHourDTO bankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();
        bankOfHourDTO.setDateWorked(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(BANK_OF_HOUR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bankOfHourDTO)))
                .andExpect(status().isBadRequest());
    }

}
