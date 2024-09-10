package com.emmeni.innov.domain.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.emmeni.innov.domain.User;
import com.emmeni.innov.domain.dto.UserDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends CustomMapper<User, UserDto> {
	
}
