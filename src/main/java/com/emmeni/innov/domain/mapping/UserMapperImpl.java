package com.emmeni.innov.domain.mapping;

import org.springframework.stereotype.Component;

import com.emmeni.innov.domain.User;
import com.emmeni.innov.domain.dto.UserDto;

@Component
public class UserMapperImpl implements UserMapper {

	@Override
	public UserDto toDto(User source) {
		return source.entity();
	}

	@Override
	public User fromDto(UserDto destination) {
		return destination.entity();
	}

}
