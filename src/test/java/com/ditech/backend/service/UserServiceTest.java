package com.ditech.backend.service;

import com.ditech.backend.dto.UserCreateRequestDto;
import com.ditech.backend.dto.UserResponseDto;
import com.ditech.backend.exception.UserNotFoundException;
import com.ditech.backend.mapper.UserMapper;
import com.ditech.backend.model.User;
import com.ditech.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User mockUser;
    private UserCreateRequestDto mockCreateRequestDto;
    private UserResponseDto mockResponseDto;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@ejemplo.com");
        mockUser.setActive(true);
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUpdatedAt(LocalDateTime.now());

        mockCreateRequestDto = new UserCreateRequestDto();
        mockCreateRequestDto.setUsername("testuser");
        mockCreateRequestDto.setEmail("test@ejemplo.com");
        mockCreateRequestDto.setActive(true);

        mockResponseDto = new UserResponseDto();
        mockResponseDto.setId(1L);
        mockResponseDto.setUsername("testuser");
        mockResponseDto.setEmail("test@ejemplo.com");
    }

    @Test
    void createUser_ShouldReturnUserResponseDto_WhenValidDataProvided() {
        // Given
        User userToSave = new User();
        userToSave.setUsername("testuser");
        userToSave.setEmail("test@ejemplo.com");
        userToSave.setActive(true);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@ejemplo.com");
        savedUser.setActive(true);
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser.setUpdatedAt(LocalDateTime.now());

        // When
        when(userMapper.toEntity(mockCreateRequestDto)).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(savedUser);
        when(userMapper.toResponseDto(savedUser)).thenReturn(mockResponseDto);

        UserResponseDto result = userService.createUser(mockCreateRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@ejemplo.com", result.getEmail());


        // Verificar que se llamaron los métodos correctos
        verify(userMapper, times(1)).toEntity(mockCreateRequestDto);
        verify(userRepository, times(1)).save(userToSave);
        verify(userMapper, times(1)).toResponseDto(savedUser);
    }

    @Test
    void createUser_ShouldCallRepositorySave_WhenValidDataProvided() {
        // Given
        User userToSave = new User();
        userToSave.setUsername("newuser");
        userToSave.setEmail("newuser@ejemplo.com");
        userToSave.setActive(true);

        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("newuser@ejemplo.com");
        savedUser.setActive(true);

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setId(2L);
        expectedResponse.setUsername("newuser");
        expectedResponse.setEmail("newuser@ejemplo.com");

        // When
        when(userMapper.toEntity(any(UserCreateRequestDto.class))).thenReturn(userToSave);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(expectedResponse);

        UserResponseDto result = userService.createUser(mockCreateRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("newuser", result.getUsername());

        // Verificar interacciones con mocks
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toEntity(any(UserCreateRequestDto.class));
        verify(userMapper, times(1)).toResponseDto(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUserResponseDto_WhenUserExists() {
        // Given
        Long userId = 1L;

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userMapper.toResponseDto(mockUser)).thenReturn(mockResponseDto);

        UserResponseDto result = userService.getUserById(userId);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@ejemplo.com", result.getEmail());

        // Verificar que se llamaron los métodos correctos
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toResponseDto(mockUser);
    }

    @Test
    void getUserById_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Given
        Long nonExistentUserId = 999L;

        // When
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Then
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> userService.getUserById(nonExistentUserId)
        );

        assertEquals("Usuario no encontrado con ID: " + nonExistentUserId, exception.getMessage());

        // Verificar que se llamó findById pero no toResponseDto
        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(userMapper, never()).toResponseDto(any(User.class));
    }

    @Test
    void getUserById_ShouldCallRepositoryFindById_WhenCalled() {
        // Given
        Long userId = 5L;
        User foundUser = new User();
        foundUser.setId(userId);
        foundUser.setUsername("founduser");
        foundUser.setEmail("found@ejemplo.com");
        foundUser.setActive(false);

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setId(userId);
        expectedResponse.setUsername("founduser");
        expectedResponse.setEmail("found@ejemplo.com");

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(foundUser));
        when(userMapper.toResponseDto(foundUser)).thenReturn(expectedResponse);

        UserResponseDto result = userService.getUserById(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("founduser", result.getUsername());

        // Verificar interacciones específicas
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toResponseDto(foundUser);
        verifyNoMoreInteractions(userRepository, userMapper);
    }
    
    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Given
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("userToDelete");
        existingUser.setEmail("delete@ejemplo.com");
        existingUser.setActive(true);

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).delete(existingUser);

        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(existingUser);
        verifyNoMoreInteractions(userRepository);
    }
    
    @Test
    void deleteUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Given
        Long userId = 999L;

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, 
            () -> userService.deleteUser(userId));
        
        assertEquals("Usuario no encontrado con ID: " + userId, exception.getMessage());
        
        // Verificar que se llamó findById pero NO delete
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }
}