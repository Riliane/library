package com.example.library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.example.library.models.LibraryUser;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		
		http.httpBasic().disable().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/signin").permitAll()
				.antMatchers(HttpMethod.GET, "/books/**").permitAll()
				.antMatchers(HttpMethod.DELETE, "/books/**").hasAnyAuthority(LibraryUser.ADMIN, LibraryUser.LIBRARIAN)
				.antMatchers(HttpMethod.POST, "/books").hasAnyAuthority(LibraryUser.ADMIN, LibraryUser.LIBRARIAN)
				.antMatchers(HttpMethod.PUT, "/books/**").hasAnyAuthority(LibraryUser.ADMIN, LibraryUser.LIBRARIAN)
				.antMatchers(HttpMethod.PUT, "/books/**").hasAnyAuthority(LibraryUser.ADMIN, LibraryUser.LIBRARIAN)
				.antMatchers(HttpMethod.GET, "/users").hasAnyAuthority(LibraryUser.ADMIN, LibraryUser.LIBRARIAN)
				.antMatchers(HttpMethod.POST, "/users").hasAnyAuthority(LibraryUser.ADMIN, LibraryUser.LIBRARIAN)
				.antMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority(LibraryUser.ADMIN, LibraryUser.LIBRARIAN)
				.anyRequest().authenticated().and()
				.apply(new JwtConfigurer(jwtTokenProvider));
		// @formatter:on
	}
}