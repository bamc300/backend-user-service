package com.ditech.backend.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    
    private Long id;
    private String username;
    private String email;
}