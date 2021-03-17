package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new MyUserDetailsService();
	}
	
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService());
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}
	
	@Bean
	CustomLogoutHandler customLogoutHandler() {
		return new CustomLogoutHandler();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws 	Exception{
		auth.authenticationProvider(authenticationProvider());
	}
	
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/public/**","/images/**",
                		"/css/**", "/js/**","/register","register","index",
                		"/about", "/contact","/contact?mailSuccess", "/allproducts",
                		"/allproducts/**", "/allProductsSearch/**")
                .permitAll()
                .antMatchers("/korisnik/**")
                .hasAnyRole("ADMIN","USER")
                .antMatchers("/gazda/**").hasRole("ADMIN")
                .anyRequest().authenticated();
                
        http
		.csrf().disable().cors().disable()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .addLogoutHandler(customLogoutHandler())
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .deleteCookies("remember-me")
                .permitAll()
                .and()
                .rememberMe()
                .userDetailsService(userDetailsService());
        		
    }
	

}
