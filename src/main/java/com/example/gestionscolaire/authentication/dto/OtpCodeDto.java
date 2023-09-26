package com.example.gestionscolaire.authentication.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class OtpCodeDto implements Serializable {
    public String code;
    public String telephone;
    public String email;
}
