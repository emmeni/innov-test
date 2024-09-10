package com.emmeni.innov.domain;

import java.util.ArrayList;
import java.util.List;

import com.emmeni.innov.domain.base.Persistent;
import com.emmeni.innov.domain.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "User")
@Table(name = "INNOV_USER")
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends Persistent{

	private static final long serialVersionUID = -2106607681451904807L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String email;
	
	@JsonIgnore
	private String password;

	@JsonIgnore
	@ToString.Exclude
	@OneToMany(cascade = jakarta.persistence.CascadeType.ALL, mappedBy = "user")
	@Builder.Default
	private List<Ticket> tickets = new ArrayList<Ticket>();

	@PostLoad
	public void initListChangeHandler() {
		tickets.size();
	}
	
	public UserDto entity() {
		UserDto user = new UserDto();
		user.setId(id);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		return user;
	}

	@Override
	public User copy() {
		User user = User.builder().id(id).username(username).email(email).password(password).tickets(tickets).build();
		return user;
	}

}
