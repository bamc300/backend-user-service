package com.ditech.backend.controller;

import com.ditech.backend.dto.UserCreateRequestDto;
import com.ditech.backend.dto.UserResponseDto;
import com.ditech.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDto mockUserResponse;
    private UserCreateRequestDto mockCreateRequest;
    private List<UserResponseDto> mockUsersList;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        mockUserResponse = new UserResponseDto();
        mockUserResponse.setId(1L);
        mockUserResponse.setUsername("testuser");
        mockUserResponse.setEmail("test@ejemplo.com");

        mockCreateRequest = new UserCreateRequestDto();
        mockCreateRequest.setUsername("testuser");
        mockCreateRequest.setEmail("test@ejemplo.com");
        mockCreateRequest.setActive(true);

        // Lista de usuarios para GET /users
        UserResponseDto user1 = new UserResponseDto();
        user1.setId(1L);
        user1.setUsername("usuario1");
        user1.setEmail("usuario1@ejemplo.com");

        UserResponseDto user2 = new UserResponseDto();
        user2.setId(2L);
        user2.setUsername("usuario2");
        user2.setEmail("usuario2@ejemplo.com");
        mockUsersList = Arrays.asList(user1, user2);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers_WhenServiceReturnsUsers() throws Exception {
        // Given
        when(userService.getAllUsers()).thenReturn(mockUsersList);

        // When & Then
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("usuario1"))
                .andExpect(jsonPath("$[0].email").value("usuario1@ejemplo.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("usuario2"))
                .andExpect(jsonPath("$[1].email").value("usuario2@ejemplo.com"));

        // Verificar que se llamó al servicio
        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenServiceReturnsEmptyList() throws Exception {
        // Given
        List<UserResponseDto> emptyList = Arrays.asList();
        when(userService.getAllUsers()).thenReturn(emptyList);

        // When & Then
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        // Verificar interacciones con el servicio
        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void createUser_ShouldReturnCreatedUser_WhenValidDataProvided() throws Exception {
        // Given
        when(userService.createUser(any(UserCreateRequestDto.class))).thenReturn(mockUserResponse);

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@ejemplo.com"));

        // Verificar que se llamó al servicio con los datos correctos
        verify(userService, times(1)).createUser(any(UserCreateRequestDto.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void createUser_ShouldCallServiceCreateUser_WhenValidRequestReceived() throws Exception {
        // Given
        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setId(5L);
        expectedResponse.setUsername("newuser");
        expectedResponse.setEmail("newuser@ejemplo.com");

        UserCreateRequestDto requestDto = new UserCreateRequestDto();
        requestDto.setUsername("newuser");
        requestDto.setEmail("newuser@ejemplo.com");
        requestDto.setActive(true);

        when(userService.createUser(any(UserCreateRequestDto.class))).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@ejemplo.com"));

        // Verificar interacciones específicas con el servicio
        verify(userService, times(1)).createUser(any(UserCreateRequestDto.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenInvalidDataProvided() throws Exception {
        // Given - DTO con datos inválidos
        UserCreateRequestDto invalidRequest = new UserCreateRequestDto();
        invalidRequest.setUsername(""); // Username vacío (inválido)
        invalidRequest.setEmail("invalid-email"); // Email inválido
        invalidRequest.setActive(null); // Active null (inválido)

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verificar que NO se llamó al servicio debido a validación fallida
        verify(userService, never()).createUser(any(UserCreateRequestDto.class));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenMissingRequiredFields() throws Exception {
        // Given - DTO con campos requeridos faltantes
        UserCreateRequestDto incompleteRequest = new UserCreateRequestDto();
        incompleteRequest.setUsername("validuser");
        // email faltante
        // active faltante

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteRequest)))
                .andExpect(status().isBadRequest());

        // Verificar que NO se llamó al servicio
        verify(userService, never()).createUser(any(UserCreateRequestDto.class));
    }
    
    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserExists() throws Exception {
        // Given
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        // When & Then
        mockMvc.perform(delete("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verificar que se llamó al servicio
        verify(userService, times(1)).deleteUser(userId);
        verifyNoMoreInteractions(userService);
    }
    
    @Test
    void deleteUser_ShouldCallServiceDeleteUser_WhenValidIdProvided() throws Exception {
        // Given
        Long userId = 5L;
        doNothing().when(userService).deleteUser(userId);

        // When & Then
        mockMvc.perform(delete("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verificar interacciones específicas con el servicio
        verify(userService, times(1)).deleteUser(userId);
        verifyNoMoreInteractions(userService);
    }
}