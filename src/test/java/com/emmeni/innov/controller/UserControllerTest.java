package com.emmeni.innov.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.emmeni.innov.domain.User;
import com.emmeni.innov.domain.dto.UserDto;
import com.emmeni.innov.domain.mapping.UserMapper;
import com.emmeni.innov.domain.mapping.UserMapperImpl;
import com.emmeni.innov.repository.UserRepository;
import com.emmeni.innov.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest extends BaseController<User, UserDto> {

	@Mock
    private MockMvc mockMvc;
	@Mock
    private UserMapper userMapperImpl;	
	@Mock
	private UserRepository userRepository;
	@MockBean
    private UserMapperImpl userMapper;
	@MockBean
	protected MessageSource messageSource;	
	@Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    UserController userController;
    @Mock
    UserService userService;

	private User user;
	
	@Override
	protected UserService getService() {
		return userService;
	}
	
	@Override
	protected UserMapper getMapper() {
		return userMapper;
	}

	@BeforeEach
	@DisplayName("Initialise mocks objects for user controller and create one ticket")
    void setup(){
        user = User.builder().id(1L).password("choice007").username("tintin").email("tintin@gmail.com").password("tintin").tickets(List.of()).build();
    }
	
    @Test
    public void testSaveUser() throws Exception {

    	given(userService.getById(1L)).willReturn(user);
    	given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));
    	given(userService.save(any(User.class), Mockito.eq(Locale.ENGLISH))).willReturn(user);
    	
        ResponseEntity<?> response = userController.save(user.entity());

        assertThat(Objects.equals(response.getStatusCode().value(), 201)).isTrue();
    }

}
