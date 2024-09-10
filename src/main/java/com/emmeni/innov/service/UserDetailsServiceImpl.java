package com.emmeni.innov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.emmeni.innov.domain.CustomUserDetails;
import com.emmeni.innov.domain.User;
import com.emmeni.innov.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with username or email: '%s'.", username)));
		
        return new CustomUserDetails(user);
	}

}
