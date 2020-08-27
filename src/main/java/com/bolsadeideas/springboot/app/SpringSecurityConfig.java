package com.bolsadeideas.springboot.app;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private LoginSuccesHandler successHandler;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests().antMatchers("/","/css/**","/images/**","/js/**","/listar").permitAll() //ejecuta un interceptor y valida permisos
		//.antMatchers("/ver/**").hasAnyRole("USER")
		//.antMatchers("/uploads/**").hasAnyRole("USER")
		//.antMatchers("/form/**").hasAnyRole("ADMIN")
		//.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		//.antMatchers("/factura/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and()
			.formLogin()
				.successHandler(successHandler)
				.loginPage("/login") //aca digo que page responde el login
				.permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403"); //mapeo a pagina de acceso denegado
	}

	
	
	@Autowired //para inyectar authentication
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		builder.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
	}

}
