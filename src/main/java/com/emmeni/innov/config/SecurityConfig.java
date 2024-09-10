package com.emmeni.innov.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.emmeni.innov.security.JWTConfigurer;
import com.emmeni.innov.security.TokenProvider;
import com.emmeni.innov.security.auth.Http401UnauthorizedEntryPoint;
import com.emmeni.innov.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    private InnovProperties innovProperties;

    @Autowired
    private UserDetailsServiceImpl jwtUserDetailsService;

    @Autowired
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;
	
	@Bean
    LocaleResolver localeResolver() {
        /*AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;*/
        SessionLocaleResolver resolver=new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    } 
	 
	@Bean 
	public ResourceBundleMessageSource messageSource() { 
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource(); 
	    messageSource.setBasename("messages");
	    messageSource.setUseCodeAsDefaultMessage(true);
	    messageSource.setDefaultEncoding("UTF-8");
	    messageSource.setAlwaysUseMessageFormat(true);
	    return messageSource; 
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor changeInterceptor = new LocaleChangeInterceptor();
	    changeInterceptor.setParamName("lang");
	    return changeInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(localeChangeInterceptor());
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jwtUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @SuppressWarnings("removal")
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
		List<String> mathers = getAuthorizePathMathers();
		
		http
    	.exceptionHandling(except -> except.authenticationEntryPoint(authenticationEntryPoint))
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .headers(head -> head.frameOptions(frmOpt -> frmOpt.disable()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .authorizeHttpRequests(exchange -> exchange
        		.requestMatchers(mathers.toArray(new String[mathers.size()])).permitAll()
        		.anyRequest().authenticated()
    		)
        .apply(securityConfigurerAdapter());
        
        return http.build();
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
	
	protected List<String> getAuthorizePathMathers() {
		return Arrays.asList("/login", "users", "/api/**",
				"/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/h2-console/**",
                "/webjars/**",
                "/file/**",
                "/swagger-ui.html");
	}
    
    @Bean
    @ConditionalOnProperty(name = "innov.cors.allowed-origins")
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = innovProperties.getCors();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
