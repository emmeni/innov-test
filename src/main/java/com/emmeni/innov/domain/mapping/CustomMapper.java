package com.emmeni.innov.domain.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomMapper<P, T> {	
	T toDto(P source);
	P fromDto(T destination);
}
