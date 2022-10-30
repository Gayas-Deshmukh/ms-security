package com.weshopify.platform.features.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WeshopifyPlatformSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private WeshopifyUserDeatilsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http
		.authorizeHttpRequests()
		.antMatchers("/","/home","/selfReg-customer","/selfReg","/error")
		.permitAll()
		.antMatchers("/customer-admin-reg",
				"/customer",
				"/view-customers",
				"/view-customers/**",
				"/delete-customers",
				"/delete-customers/**",
				"/create-customer",
				"/edit-customers/",
				"/search-customers",
				"/access-denied")
		.authenticated()
		.and().csrf().disable()
		.formLogin()
		.loginPage("/login")
		.defaultSuccessUrl("/view-customers")
		.usernameParameter("userName")
		.passwordParameter("password")
		.and()
		.logout()
		.logoutUrl("/logout")
		.logoutSuccessUrl("/home")
		.and()
		.exceptionHandling()
		.accessDeniedPage("/access-denied");

	}
}
