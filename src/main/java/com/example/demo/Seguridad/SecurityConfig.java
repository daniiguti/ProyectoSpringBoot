package com.example.demo.Seguridad;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

//A partir de una version de spring, (>3.0.4)  WebSecurityConfigurerAdapter se elimino
//por lo que en esta estamos usando la 2.7.9 que en esta sigue estando disponible
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.passwordEncoder(NoOpPasswordEncoder.getInstance())
			.withUser("admin")
			.password("admin")
			.roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			//autoriza las peticiones
			.authorizeRequests()
			//a cualquier pagina que se encuentre en esa carpeta, autoriza a todos
			.antMatchers("/webjars/**", "/css/**", "/h2-console/**").permitAll()
			//las demas paginas, tienen que autentificarse
			.anyRequest().authenticated()
			//y
			.and()
		//el formulario de login	
		.formLogin()
			//es este
			.loginPage("/login")
			//permitelos a todos
			.permitAll();
		
		
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
	
	
	
}
