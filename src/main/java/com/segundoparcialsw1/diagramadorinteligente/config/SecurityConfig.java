package com.segundoparcialsw1.diagramadorinteligente.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//d/asds
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> {
		})
			.authorizeHttpRequests(auth -> auth.requestMatchers("/usuarios/**")
				.permitAll()
				.requestMatchers("/diagramas/**")
				.permitAll()
				.requestMatchers("/api/codegen/**")
				.permitAll()
				.requestMatchers("/ia/**")
				.permitAll()
				.requestMatchers("/colaboradores/**")
				.permitAll()
				.anyRequest()
				.authenticated())
			.httpBasic(httpBasic -> httpBasic.disable())
			.formLogin(form -> form.disable());

		return http.build();
	}

}
