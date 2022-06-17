package com.example.testclient.customUser;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.testclient.filter.JwtRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private JwtRequestFilter jwtrequestfilter;
	

//	@Autowired
//    private DataSource dataSource;
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.cors().and().csrf().disable()
    		.authorizeRequests().antMatchers("/dept_photos/{id}/{photo}","/getmail/{mail}","/resetpassword","/forgotpassword/{username}","/registerapii","/process_register","/verify","/deleteApi/{id}","/deleteApi","/all","api/order").permitAll().anyRequest().authenticated()
    		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    	http.addFilterBefore(jwtrequestfilter, UsernamePasswordAuthenticationFilter.class);
//    	http
//    	.csrf().disable()
//    	.authorizeRequests()
//        .antMatchers("/users").authenticated()
//        .anyRequest().permitAll()
//        .and()
//        .formLogin()
//            .usernameParameter("email")
//            .defaultSuccessUrl("/users")
//            .permitAll()
//        .and()
//        .logout().logoutSuccessUrl("/").permitAll();
    }
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean()throws Exception
	{
		return super.authenticationManagerBean();
	}

    
}