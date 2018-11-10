package com.lukhol.dna.exercise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukhol.dna.exercise.dto.UserDto;
import com.lukhol.dna.exercise.errors.ServiceValidationException;
import com.lukhol.dna.exercise.model.User;
import com.lukhol.dna.exercise.service.UserService;
import com.lukhol.test.config.WithMockCustomUser;
import org.hamcrest.core.IsNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTests {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void canGetUsers_success() throws Exception {
        var users = List.of(
                User.builder().id(1L).login("login1").password("password").build(),
                User.builder().id(2L).login("login2").password("password").build()
        );
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].login", is("login1")))
                .andExpect(jsonPath("$[0].password", is("password")))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].login", is("login2")))
                .andExpect(jsonPath("$[1].password", is("password")))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    public void cannotGetUserById_notFound() throws Exception {
        when(userService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void canGetUserById_success() throws Exception {
        when(userService.findById(anyLong())).thenReturn(
                Optional.of(User.builder().id(1L).login("login").password("password").build())
        );

        mockMvc
                .perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is("login")))
                .andExpect(jsonPath("$.password", is("password")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void cannotCreateUser_creationError() throws Exception {
        when(userService.create(ArgumentMatchers.any(UserDto.class))).thenThrow(ServiceValidationException.class);
        UserDto dto = UserDto.builder().login("login").password("password").build();

        var perform = MockMvcRequestBuilders.
                post("/users")
                .content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc
                .perform(perform)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void canCreateUser_success() throws Exception {
        User user = User.builder().id(1L).login("login").password("password").build();
        UserDto dto = UserDto.builder().login("login").password("password").build();
        when(userService.create(ArgumentMatchers.any(UserDto.class))).thenReturn(user);

        var perform = MockMvcRequestBuilders
                .post("/users")
                .content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON);

        var mvcResult = mockMvc.perform(perform)
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(
                mvcResult
                        .getResponse()
                        .getHeader("Location")
        ).isEqualTo("http://localhost/users/1");
    }

    @Test
    public void canUpdate_success() throws Exception {
        UserDto dto = UserDto.builder().login("login").password("password").build();
        User user = User.builder().id(1L).login("newLogin").password("newPassword").build();

        when(userService.update(anyLong(), ArgumentMatchers.any(UserDto.class))).thenReturn(user);

        var perform = MockMvcRequestBuilders
                .put("/users/1")
                .content(mapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(perform)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.login", is("newLogin")))
                .andExpect(jsonPath("$.password", is(IsNull.nullValue())));
    }

    @Test
    public void cannotDeleteUser_noAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser
    public void canDeleteUser_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1))
                .removeUserById(anyLong(), ArgumentMatchers.any(User.class));

        verifyNoMoreInteractions(userService);
    }
}