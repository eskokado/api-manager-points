package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.CompanyDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.CompanyDTO;
import com.dio.santander.apimanagerpoints.services.CompanyService;
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
public class CompanyControllerTest {
    private static final String COMPANY_API_URL_PATH = "/api/v1/companies";
    private static final Long VALID_COMPANY_ID = 1L;
    private static final Long INVALID_COMPANY_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenACompanyIsCreated() throws Exception {
        // given
        CompanyDTO companyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();

        // when
        when(companyService.insert(companyDTO)).thenReturn(companyDTO);

        // then
        mockMvc.perform(post(COMPANY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(companyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(companyDTO.getDescription()))
                .andExpect(jsonPath("$.address").value(companyDTO.getAddress()))
                .andExpect(jsonPath("$.cnpj").value(companyDTO.getCnpj()))
                .andExpect(jsonPath("$.city").value(companyDTO.getCity()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        CompanyDTO companyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();
        companyDTO.setDescription(null);

        // then
        mockMvc.perform(post(COMPANY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(companyDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        CompanyDTO companyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();

        // when
        when(companyService.find(VALID_COMPANY_ID)).thenReturn(companyDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(COMPANY_API_URL_PATH + "/" + VALID_COMPANY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(companyDTO.getDescription()))
                .andExpect(jsonPath("$.address").value(companyDTO.getAddress()))
                .andExpect(jsonPath("$.cnpj").value(companyDTO.getCnpj()))
                .andExpect(jsonPath("$.city").value(companyDTO.getCity()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(companyService.find(INVALID_COMPANY_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(COMPANY_API_URL_PATH + "/" + INVALID_COMPANY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithCompanyIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        CompanyDTO companyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();

        // when
        when(companyService.findAll()).thenReturn(Collections.singletonList(companyDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(COMPANY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value(companyDTO.getDescription()))
                .andExpect(jsonPath("$[0].address").value(companyDTO.getAddress()))
                .andExpect(jsonPath("$[0].cnpj").value(companyDTO.getCnpj()))
                .andExpect(jsonPath("$[0].city").value(companyDTO.getCity()));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(companyService).delete(VALID_COMPANY_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(COMPANY_API_URL_PATH + "/" + VALID_COMPANY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(companyService).delete(INVALID_COMPANY_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(COMPANY_API_URL_PATH + "/" + INVALID_COMPANY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenACompanyIsOkStatusReturned() throws Exception {
        // given
        CompanyDTO companyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();
        companyDTO.setDescription("Modified Name");
        // when
        when(companyService.update(companyDTO)).thenReturn(companyDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(COMPANY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(companyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Modified Name")));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        CompanyDTO companyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();

        // when
        when(companyService.update(companyDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(COMPANY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(companyDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        CompanyDTO companyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();
        companyDTO.setDescription(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(COMPANY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(companyDTO)))
                .andExpect(status().isBadRequest());
    }

}
