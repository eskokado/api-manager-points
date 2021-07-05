package com.dio.santander.apimanagerpoints.controllers;

import com.dio.santander.apimanagerpoints.builders.UserDTOBuilder;
import com.dio.santander.apimanagerpoints.controllers.exceptions.ResourceExceptionHandler;
import com.dio.santander.apimanagerpoints.dtos.UserDTO;
import com.dio.santander.apimanagerpoints.services.UserService;
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
public class UserControllerTest {
    private static final String USER_API_URL_PATH = "/api/v1/users";
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_USER_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ResourceExceptionHandler())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAUserIsCreated() throws Exception {
        // given
        UserDTO userDTO = UserDTOBuilder.builder().build().toUserDTO();

        // when
        when(userService.insert(userDTO)).thenReturn(userDTO);

        // then
        mockMvc.perform(post(USER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessLevel.description").value(userDTO.getAccessLevel().getDescription()))
                .andExpect(jsonPath("$.userCategory.description").value(userDTO.getUserCategory().getDescription()))
                .andExpect(jsonPath("$.company.description").value(userDTO.getCompany().getDescription()))
                .andExpect(jsonPath("$.tolerance").value(userDTO.getTolerance()))
                .andExpect(jsonPath("$.workDayStart").value(userDTO.getWorkDayStart()));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        UserDTO userDTO = UserDTOBuilder.builder().build().toUserDTO();
        userDTO.setName(null);

        // then
        mockMvc.perform(post(USER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // given
        UserDTO userDTO = UserDTOBuilder.builder().build().toUserDTO();

        // when
        when(userService.find(VALID_USER_ID)).thenReturn(userDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(USER_API_URL_PATH + "/" + VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.accessLevel.description").value(userDTO.getAccessLevel().getDescription()))
                .andExpect(jsonPath("$.userCategory.description").value(userDTO.getUserCategory().getDescription()))
                .andExpect(jsonPath("$.company.description").value(userDTO.getCompany().getDescription()))
                .andExpect(jsonPath("$.tolerance").value(userDTO.getTolerance()))
                .andExpect(jsonPath("$.workDayStart").value(userDTO.getWorkDayStart()));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        when(userService.find(INVALID_USER_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(USER_API_URL_PATH + "/" + INVALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithUserIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        UserDTO userDTO = UserDTOBuilder.builder().build().toUserDTO();

        // when
        when(userService.findAll()).thenReturn(Collections.singletonList(userDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(USER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accessLevel.description").value(userDTO.getAccessLevel().getDescription()))
                .andExpect(jsonPath("$[0].userCategory.description").value(userDTO.getUserCategory().getDescription()))
                .andExpect(jsonPath("$[0].company.description").value(userDTO.getCompany().getDescription()))
                .andExpect(jsonPath("$[0].tolerance").value(userDTO.getTolerance()))
                .andExpect(jsonPath("$[0].workDayStart").value(userDTO.getWorkDayStart()));
    }

    @Test
    void thenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // when
        doNothing().when(userService).delete(VALID_USER_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_API_URL_PATH + "/" + VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void thenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(ObjectNotFoundException.class).when(userService).delete(INVALID_USER_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_API_URL_PATH + "/" + INVALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTIsCalledThenAUserIsOkStatusReturned() throws Exception {
        // given
        UserDTO userDTO = UserDTOBuilder.builder().build().toUserDTO();
        userDTO.setName("Modified Name");
        // when
        when(userService.update(userDTO)).thenReturn(userDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(USER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Modified Name")));
    }

    @Test
    void thenPUTIsCalledWithInvalidIdIsNotFoundStatusReturned() throws Exception {
        // given
        UserDTO userDTO = UserDTOBuilder.builder().build().toUserDTO();

        // when
        when(userService.update(userDTO)).thenThrow(ObjectNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(USER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void thenPUTisCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        UserDTO userDTO = UserDTOBuilder.builder().build().toUserDTO();
        userDTO.setName(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put(USER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest());
    }

}
