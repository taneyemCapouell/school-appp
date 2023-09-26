package com.example.gestionscolaire.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class DefaultResponseDto {
    String message;
    HttpStatus status_code;
}
