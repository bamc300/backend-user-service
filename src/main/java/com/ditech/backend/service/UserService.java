package com.ditech.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ditech.backend.dto.UserCreateRequestDto;
import com.ditech.backend.dto.UserResponseDto;
import com.ditech.backend.exception.UserNotFoundException;
import com.ditech.backend.mapper.UserMapper;
import com.ditech.backend.model.User;
import com.ditech.backend.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * Crear un nuevo usuario
     * @param userCreateRequestDto Datos del usuario a crear
     * @return DTO del usuario creado
     */
    public UserResponseDto createUser(UserCreateRequestDto userCreateRequestDto) {
        User user = userMapper.toEntity(userCreateRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }
    
    /**
     * Obtener todos los usuarios
     * @return Lista de DTOs de usuarios
     */
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener un usuario por ID
     * @param id ID del usuario
     * @return DTO del usuario encontrado
     * @throws UserNotFoundException si no se encuentra el usuario
     */
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponseDto(user);
    }
    
    /**
     * Eliminar un usuario por ID
     * @param id ID del usuario a eliminar
     * @throws UserNotFoundException si no se encuentra el usuario
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
