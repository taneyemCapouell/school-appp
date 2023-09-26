package com.example.gestionscolaire.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthReqDto {

	@Schema(description = "nom d'utilisateur (email)")
	@NotBlank
	private String login;
	
	@NotBlank
	private String password;
	
}
