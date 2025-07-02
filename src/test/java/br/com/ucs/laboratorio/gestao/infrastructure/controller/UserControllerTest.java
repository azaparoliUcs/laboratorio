package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.UserDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.TokenResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.UserResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.UserModel;
import br.com.ucs.laboratorio.gestao.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserDto userDto;
    private UserResponse userResponse;
    private TokenResponse tokenResponse;
    private UserModel user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        userDto = new UserDto();
        userDto.setEmail("testuser");
        userDto.setPassword("password123");
        userDto.setEmail("test@example.com");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("testuser");
        userResponse.setEmail("test@example.com");

        tokenResponse = new TokenResponse();
        tokenResponse.setToken("jwt-token-123");

        user = new UserModel();
        user.setId(1L);
        user.setEmail("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    void login_ShouldReturnTokenResponse_WhenValidCredentials() throws Exception {
        when(userService.authenticateUser(any(UserDto.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService).authenticateUser(any(UserDto.class));
    }

    @Test
    void createUser_ShouldReturnUserResponse_WhenValidUserDto() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userResponse);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService).createUser(any(UserDto.class));
    }

    @Test
    void findById_ShouldReturnUserResponse_WhenUserExists() throws Exception {
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk());

        verify(userService).findById(1L);
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNoContent());

        verify(userService).delete(1L);
    }

    @Test
    void update_ShouldReturnUpdatedUserResponse() throws Exception {
        when(userService.update(eq(1L), any(UserDto.class))).thenReturn(userResponse);

        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService).update(eq(1L), any(UserDto.class));
    }
}