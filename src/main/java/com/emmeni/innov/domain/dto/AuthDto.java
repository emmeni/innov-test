package com.emmeni.innov.domain.dto;

import com.emmeni.innov.config.Constants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthDto {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    @NotBlank
    @Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH)
    private String username;

    @NotNull
    @NotBlank
    @Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH)
    private String password;

    private Boolean rememberMe;

}
