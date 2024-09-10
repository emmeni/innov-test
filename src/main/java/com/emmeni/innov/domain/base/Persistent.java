package com.emmeni.innov.domain.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@jakarta.persistence.MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class Persistent implements IPersistent {

	public abstract Long getId();

	public abstract void setId(Long val);

	public abstract Persistent copy();

	@JsonIgnore
	public boolean isPersistent() {
		return getId() != null;
	}
	
	@Override
	public String toString() {
		return "Id = " + getId();
	}

}
