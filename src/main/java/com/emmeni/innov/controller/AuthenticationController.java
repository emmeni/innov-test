package com.emmeni.innov.controller;

import java.util.Collections;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import com.emmeni.innov.domain.dto.AuthDto;
import com.emmeni.innov.security.JWTConfigurer;
import com.emmeni.innov.security.JWTToken;
import com.emmeni.innov.security.TokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping( value = "/api", produces = MediaType.APPLICATION_JSON_VALUE )
@Tag(name = "Authentication", description = "The Authentication API. Contains the main and unique operation that generate the token for an user.")
@Validated
public class AuthenticationController {

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @Operation(summary = "User authenticate", description = "Get user token when given credentials")
    public ResponseEntity<?> authorize(@Valid @RequestBody AuthDto authDto, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (authDto.getRememberMe() == null) ? false : authDto.getRememberMe();
            String jwt = tokenProvider.generateToken(authentication, rememberMe);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException", ex.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/change-language")
    @Operation(summary = "User language", description = "Choose the appropriate language for the server message")
    public ResponseEntity<?> changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        Locale newLocale = new Locale(lang);
        localeResolver.setLocale(request, response, newLocale);
        return ResponseEntity.ok(true);
    }

}
