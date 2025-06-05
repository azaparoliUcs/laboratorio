package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.application.configuration.auth.JwtUtil;
import br.com.ucs.laboratorio.gestao.application.configuration.auth.UserDetailsImpl;
import br.com.ucs.laboratorio.gestao.domain.dto.UserDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.UserResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.domain.entity.UserModel;
import br.com.ucs.laboratorio.gestao.domain.type.UserType;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.UserRepository;
import br.com.ucs.laboratorio.gestao.application.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LaboratoryService laboratoryService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtTokenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_success() {
        UserDto userDto = new UserDto();
        userDto.setEmail("email@test.com");
        userDto.setPassword("password");

        Authentication auth = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenService.generateToken(userDetails)).thenReturn("jwt-token");

        String token = userService.authenticateUser(userDto);

        assertEquals("jwt-token", token);
    }

    @Test
    void testCreateUser_success() {
        UserDto userDto = new UserDto();
        userDto.setEmail("newuser@test.com");
        userDto.setPassword("rawpass");
        userDto.setLaboratoryId(1L);

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(laboratoryService.findById(1L)).thenReturn(new LaboratoryModel());

        UserModel userModel = new UserModel();
        userModel.setEmail(userDto.getEmail());

        UserModel savedUser = new UserModel();
        UserResponse response = new UserResponse();

        when(modelMapper.map(userDto, UserModel.class)).thenReturn(userModel);
        when(userRepository.save(userModel)).thenReturn(savedUser);
        when(modelMapper.map(savedUser, UserResponse.class)).thenReturn(response);

        UserResponse result = userService.createUser(userDto);

        assertEquals(response, result);
        assertNotNull(userModel.getPassword());
    }

    @Test
    void testCreateUser_alreadyExists() {
        UserDto dto = new UserDto();
        dto.setEmail("existing@test.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new UserModel()));

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.createUser(dto));
        assertEquals("Usuario ja existe", ex.getMessage());
    }

    @Test
    void testFindById_success() {
        Long id = 1L;
        UserModel user = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserModel result = userService.findById(id);

        assertEquals(user, result);
    }

    @Test
    void testFindById_notFound() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> userService.findById(100L));
        assertEquals("Usuario nao existe", ex.getMessage());
    }

    @Test
    void testDelete_success() {
        Long id = 1L;
        UserModel user = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.delete(id);

        verify(userRepository).delete(user);
    }

    @Test
    void testUpdate_success() {
        Long id = 1L;
        UserDto dto = new UserDto();
        dto.setName("Novo Nome");
        dto.setUserType(UserType.RESPONSIBLE);
        dto.setLaboratoryId(2L);

        UserModel user = new UserModel();
        LaboratoryModel lab = new LaboratoryModel();
        UserModel saved = new UserModel();
        UserResponse response = new UserResponse();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(laboratoryService.findById(2L)).thenReturn(lab);
        when(userRepository.save(user)).thenReturn(saved);

        try (MockedStatic<MapperUtil> mocked = mockStatic(MapperUtil.class)) {
            mocked.when(() -> MapperUtil.mapObject(saved, UserResponse.class)).thenReturn(response);

            UserResponse result = userService.update(id, dto);

            assertEquals(response, result);
            assertEquals("Novo Nome", user.getName());
            assertEquals(UserType.RESPONSIBLE, user.getUserType());
            assertEquals(lab, user.getLaboratory());
        }
    }
}
