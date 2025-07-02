package br.com.ucs.laboratorio.gestao.domain.service;

import br.com.ucs.laboratorio.gestao.application.configuration.auth.JwtUtil;
import br.com.ucs.laboratorio.gestao.application.configuration.auth.UserDetailsImpl;
import br.com.ucs.laboratorio.gestao.domain.dto.UserDto;
import br.com.ucs.laboratorio.gestao.domain.dto.response.TokenResponse;
import br.com.ucs.laboratorio.gestao.domain.dto.response.UserResponse;
import br.com.ucs.laboratorio.gestao.domain.entity.UserModel;
import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import br.com.ucs.laboratorio.gestao.application.exception.BusinessException;
import br.com.ucs.laboratorio.gestao.domain.type.UserType;
import br.com.ucs.laboratorio.gestao.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private UserModel userModel;
    private UserResponse userResponse;
    private LaboratoryModel laboratory;
    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        // Setup UserDto
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setName("Test User");
        userDto.setLaboratoryId(1L);
        userDto.setUserType(UserType.ADMIN);

        // Setup Laboratory
        laboratory = new LaboratoryModel();
        laboratory.setId(1L);
        laboratory.setRoomName("Test Lab");

        // Setup UserModel
        userModel = new UserModel();
        userModel.setId(1L);
        userModel.setEmail("test@example.com");
        userModel.setName("Test User");
        userModel.setPassword("encodedPassword");
        userModel.setLaboratory(laboratory);
        userModel.setUserType(UserType.ADMIN);

        // Setup UserResponse
        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@example.com");
        userResponse.setName("Test User");
        userResponse.setUserType(UserType.ADMIN);

        // Setup TokenResponse
        tokenResponse = new TokenResponse();
        tokenResponse.setToken("jwt-token");
        tokenResponse.setEmail("test@example.com");
        tokenResponse.setName("Test User");
    }

    @Test
    void authenticateUser_Success() {
        // Arrange
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(userModel);
        when(jwtTokenService.generateToken(userDetails)).thenReturn("jwt-token");

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.MapperUtil> mapperUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.MapperUtil.class)) {

            mapperUtil.when(() -> br.com.ucs.laboratorio.gestao.application.util.MapperUtil
                            .mapObject(userModel, TokenResponse.class))
                    .thenReturn(tokenResponse);

            // Act
            TokenResponse result = userService.authenticateUser(userDto);

            // Assert
            assertNotNull(result);
            assertEquals("jwt-token", result.getToken());
            assertEquals("test@example.com", result.getEmail());
            assertEquals("Test User", result.getName());

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtTokenService).generateToken(userDetails);
        }
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(laboratoryService.findById(userDto.getLaboratoryId())).thenReturn(laboratory);
        when(modelMapper.map(userDto, UserModel.class)).thenReturn(userModel);
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);
        when(modelMapper.map(userModel, UserResponse.class)).thenReturn(userResponse);

        // Act
        UserResponse result = userService.createUser(userDto);

        // Assert
        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getEmail(), result.getEmail());
        assertEquals(userResponse.getName(), result.getName());

        verify(userRepository).findByEmail(userDto.getEmail());
        verify(laboratoryService).findById(userDto.getLaboratoryId());
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    void createUser_UserAlreadyExists_ThrowsBusinessException() {
        // Arrange
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(userModel));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.createUser(userDto));

        assertEquals("Usuario ja existe", exception.getMessage());
        verify(userRepository).findByEmail(userDto.getEmail());
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    void findById_Success() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));

        // Act
        UserModel result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userModel.getId(), result.getId());
        assertEquals(userModel.getEmail(), result.getEmail());
        assertEquals(userModel.getName(), result.getName());

        verify(userRepository).findById(userId);
    }

    @Test
    void findById_UserNotFound_ThrowsBusinessException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.findById(userId));

        assertEquals("Usuario nao existe", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    void delete_Success() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        doNothing().when(userRepository).delete(userModel);

        // Act
        assertDoesNotThrow(() -> userService.delete(userId));

        // Assert
        verify(userRepository).findById(userId);
        verify(userRepository).delete(userModel);
    }

    @Test
    void delete_UserNotFound_ThrowsBusinessException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.delete(userId));

        assertEquals("Usuario nao existe", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any(UserModel.class));
    }

    @Test
    void update_Success() {
        // Arrange
        Long userId = 1L;
        UserDto updateDto = new UserDto();
        updateDto.setName("Updated Name");
        updateDto.setUserType(UserType.RESPONSIBLE);
        updateDto.setLaboratoryId(2L);

        LaboratoryModel newLaboratory = new LaboratoryModel();
        newLaboratory.setId(2L);
        newLaboratory.setRoomName("New Lab");

        UserModel updatedUser = new UserModel();
        updatedUser.setId(userId);
        updatedUser.setName("Updated Name");
        updatedUser.setUserType(UserType.RESPONSIBLE);
        updatedUser.setLaboratory(newLaboratory);

        UserResponse updatedResponse = new UserResponse();
        updatedResponse.setId(userId);
        updatedResponse.setName("Updated Name");
        updatedResponse.setUserType(UserType.RESPONSIBLE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(laboratoryService.findById(updateDto.getLaboratoryId())).thenReturn(newLaboratory);
        when(userRepository.save(userModel)).thenReturn(updatedUser);

        try (MockedStatic<br.com.ucs.laboratorio.gestao.application.util.MapperUtil> mapperUtil =
                     mockStatic(br.com.ucs.laboratorio.gestao.application.util.MapperUtil.class)) {

            mapperUtil.when(() -> br.com.ucs.laboratorio.gestao.application.util.MapperUtil
                            .mapObject(updatedUser, UserResponse.class))
                    .thenReturn(updatedResponse);

            // Act
            UserResponse result = userService.update(userId, updateDto);

            // Assert
            assertNotNull(result);
            assertEquals(updatedResponse.getId(), result.getId());
            assertEquals(updatedResponse.getName(), result.getName());
            assertEquals(updatedResponse.getUserType(), result.getUserType());

            verify(userRepository).findById(userId);
            verify(laboratoryService).findById(updateDto.getLaboratoryId());
            verify(userRepository).save(userModel);
        }
    }

    @Test
    void update_UserNotFound_ThrowsBusinessException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.update(userId, userDto));

        assertEquals("Usuario nao existe", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    void authenticateUser_AuthenticationFailure() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.core.AuthenticationException("Authentication failed") {});

        // Act & Assert
        assertThrows(org.springframework.security.core.AuthenticationException.class,
                () -> userService.authenticateUser(userDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenService, never()).generateToken(any());
    }
}