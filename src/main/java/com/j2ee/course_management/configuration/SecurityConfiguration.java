package com.j2ee.course_management.configuration;

import com.j2ee.course_management.repository.UserRepository;
import com.j2ee.course_management.service.query.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
/*@EnableGlobalMethodSecurity(securedEnabled = true)*/
public class
SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;

	private static final String[] AUTH_WHITELIST = {
		"/swagger-resources/**",
		"/swagger-ui.html",
		"/v2/api-docs",
		"/webjars/**"
	};

	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers(AUTH_WHITELIST).permitAll()
				.antMatchers(HttpMethod.GET, "/**").permitAll()
				.antMatchers("/admin/**").hasAuthority("LECTURER").anyRequest()
				.authenticated().and().csrf().disable().formLogin()
				.loginPage("/login").failureUrl("/login?error=true")
				.defaultSuccessUrl("/dashboard", true)
				.and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").and().exceptionHandling()
				.accessDeniedPage("/access-denied")
				.and()
				.httpBasic()
				.authenticationEntryPoint(getBasicAuthEntryPoint())
				.and()
				.sessionManagement()
				.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS);

	}

	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
		return new CustomBasicAuthenticationEntryPoint();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
				.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**")
				.antMatchers(HttpMethod.OPTIONS, "/**");
	}

	@Override
	public UserDetailsService userDetailsServiceBean() {
		return new CustomUserDetailService(userRepository);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsServiceBean());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

}
