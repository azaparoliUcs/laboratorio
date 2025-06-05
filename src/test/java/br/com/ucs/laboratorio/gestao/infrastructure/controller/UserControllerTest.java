package br.com.ucs.laboratorio.gestao.infrastructure.controller;

import br.com.ucs.laboratorio.gestao.domain.dto.UserDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.UserResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.UserModel;
import br.com.ucs.laboratorio.gestao.domain.service.UserService;
import br.com.ucs.laboratorio.gestao.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        UserDto dto = new UserDto();
        when(userService.authenticateUser(dto)).thenReturn("token");

        var result = userController.createAuthenticationToken(dto);

        assertEquals("token", result.getBody());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testCreateUser() {
        UserDto dto = new UserDto();
        UserResponse response = new UserResponse();
        when(userService.createUser(dto)).thenReturn(response);

        var result = userController.createUser(dto);

        assertEquals(response, result.getBody());
    }

    @Test
    void testFindById() {
        try (var mocked = mockStatic(MapperUtil.class)) {
            when(userService.findById(1L)).thenReturn(new UserModel());
            mocked.when(() -> MapperUtil.mapObject(any(), eq(UserResponse.class))).thenReturn(new UserResponse());

            var result = userController.findById(1L);
            assertNotNull(result.getBody());
        }
    }

    @Test
    void testUpdate() {
        UserDto dto = new UserDto();
        when(userService.update(1L, dto)).thenReturn(new UserResponse());

        var result = userController.update(1L, dto);
        assertNotNull(result.getBody());
    }

    @Test
    void testDelete() {
        var result = userController.delete(1L);
        assertEquals(204, result.getStatusCodeValue());
        verify(userService).delete(1L);
    }
}