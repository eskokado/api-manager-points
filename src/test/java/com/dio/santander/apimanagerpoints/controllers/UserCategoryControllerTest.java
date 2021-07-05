package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.UserCategoryDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.UserCategoryDTO;
import com.dio.santander.apimanagerpoints.services.UserCategoryService;
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
public class UserCategoryControllerTest {
    private static final String USER_CATEGORY_API_URL_PATH = "/api/v1/user_categories";
    private static final Long VALID_USER_CATEGORY_ID = 1L;
    private static final Long INVALID_USER_CATEGORY_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private UserCategoryService userCategoryService;

    @InjectMocks
    private UserCategoryController userCategoryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userCategoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAUserCategoryIsCreated() throws Exception {
        // given
        UserCategoryDTO userCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();

        // when
        when(userCategoryService.insert(userCategoryDTO)).thenReturn(userCategoryDTO);

        // then
        mockMvc.perform(post(USER_CATEGORY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCategoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(userCategoryDTO.getDescription()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        UserCategoryDTO userCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
        userCategoryDTO.setDescription(null);

        // then
        mockMvc.perform(post(USER_CATEGORY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCategoryDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        UserCategoryDTO userCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();

        // when
        when(userCategoryService.find(VALID_USER_CATEGORY_ID)).thenReturn(userCategoryDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CATEGORY_API_URL_PATH + "/" + VALID_USER_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(userCategoryDTO.getDescription()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(userCategoryService.find(INVALID_USER_CATEGORY_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CATEGORY_API_URL_PATH + "/" + INVALID_USER_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithUserCategoryIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        UserCategoryDTO userCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();

        // when
        when(userCategoryService.findAll()).thenReturn(Collections.singletonList(userCategoryDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CATEGORY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(userCategoryDTO.getDescription())));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(userCategoryService).delete(VALID_USER_CATEGORY_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CATEGORY_API_URL_PATH + "/" + VALID_USER_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(userCategoryService).delete(INVALID_USER_CATEGORY_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CATEGORY_API_URL_PATH + "/" + INVALID_USER_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenAUserCategoryIsOkStatusReturned() throws Exception {
        // given
        UserCategoryDTO userCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
        userCategoryDTO.setDescription("Modified Name");
        // when
        when(userCategoryService.update(userCategoryDTO)).thenReturn(userCategoryDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CATEGORY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCategoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Modified Name")));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        UserCategoryDTO userCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();

        // when
        when(userCategoryService.update(userCategoryDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CATEGORY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCategoryDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        UserCategoryDTO userCategoryDTO = UserCategoryDTOBuilder.builder().build().toUserCategoryDTO();
        userCategoryDTO.setDescription(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CATEGORY_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCategoryDTO)))
                .andExpect(status().isBadRequest());
    }

}
