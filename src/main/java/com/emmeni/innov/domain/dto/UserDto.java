package com.emmeni.innov.domain.dto;

import com.emmeni.innov.config.Constants;
import com.emmeni.innov.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	@JsonIgnore
	private Long id;

	@Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    @NotBlank
    @Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH)
    private String username;

	@Email
    @Size(min = 5, max = 100)
    private String email;
	
	@NotNull
    @NotBlank
    @Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH)
    private String password;
	
	public User entity() {
		User user = User.builder().id(id).username(username).email(email).password(password).build();
		return user;
	}
	
}
