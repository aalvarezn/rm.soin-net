package com.soin.sgrm.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeRequests()
	        // Permitir acceso a recursos estáticos
	        .antMatchers("/static/**").permitAll()
	        // Configurar accesos por roles
	        .antMatchers("/admin/**", "/info/").hasRole("Admin")
	        .antMatchers("/management/**", "/info/").hasRole("Release Manager")
	        .antMatchers("/forgetPassword", "/recoverPassword", "/admin/request/syncExcel", "/ws/**").permitAll()
	        .antMatchers("/manager/**").permitAll()  // Asegurar que las reglas específicas van antes de `anyRequest`
	        .anyRequest().hasRole("Gestores")  // Aplicar rol específico
	        .and()
	        // Configuración de inicio de sesión
	        .formLogin()
	            .loginPage("/login")
	            .failureUrl("/login?error=true")
	            .defaultSuccessUrl("/successLogin", true)  // Redirigir siempre al éxito, manejando redirección cíclica
	            .permitAll()
	        .and()
	        // Configuración de cierre de sesión
	        .logout()
	            .logoutSuccessUrl("/login")
	            .permitAll()
	        .and()
	        // Configuración de encabezados
	        .headers()
	            .frameOptions().sameOrigin()
	        .and()
	        // Configuración de CSRF
	        .csrf()
	            .ignoringAntMatchers("/ws/**", "/manager/**");  // Combinar antMatchers para ignorar CSRF
	}
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver() {
		return new AuthenticationTrustResolverImpl();
	}

}