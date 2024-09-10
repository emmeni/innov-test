package com.emmeni.innov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.emmeni.innov.config.InnovProperties;

@SpringBootApplication
@EnableConfigurationProperties({ InnovProperties.class })
public class InnovApplication {

	public static void main(String[] args) {
		SpringApplication.run(InnovApplication.class, args);
	}

}
