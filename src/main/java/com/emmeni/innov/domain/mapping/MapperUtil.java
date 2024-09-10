package com.emmeni.innov.domain.mapping;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MapperUtil<P, T> {

	private Class<P> pClazz;
    private Class<T> tClazz;
    
    private final ObjectMapper objectMapper;

    public MapperUtil(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
    }

    public P fromDto(T fromObject) {
        return objectMapper.convertValue(fromObject, pClazz);
    }

    public T toDto(P fromObject) {
        return objectMapper.convertValue(fromObject, tClazz);
    }

    /*public <T> T convert(P fromObject, TypeReference<T> typeReference) {
        return objectMapper.convertValue(fromObject, typeReference);
    }*/
}
