package com.ditech.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.ditech.backend.dto.UserCreateRequestDto;
import com.ditech.backend.dto.UserResponseDto;
import com.ditech.backend.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    
    /**
     * Convierte un UserCreateRequestDto a una entidad User
     * @param userCreateRequestDto DTO con los datos de entrada
     * @return Entidad User
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserCreateRequestDto userCreateRequestDto);
    
    /**
     * Convierte una entidad User a UserResponseDto
     * @param user Entidad User
     * @return DTO de respuesta
     */
    UserResponseDto toResponseDto(User user);
}