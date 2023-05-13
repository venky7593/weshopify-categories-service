package com.weshopify.platform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppSecurityConfig {

	@Autowired
	private JwtAuthenticationService authnService;
	
	/*
	 * @Bean public InMemoryUserDetailsManager userDetailsService() { UserDetails
	 * user = User .withDefaultPasswordEncoder() .username("admin")
	 * .password("admin") .roles("ADMIN").build(); return new
	 * InMemoryUserDetailsManager(user); }
	 */
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> {
			try {
				authz
				        //.requestMatchers(HttpMethod.POST, "/categories/**").permitAll()
				        .anyRequest().authenticated()
				        .and().csrf().disable().anonymous().disable()
				        .addFilterBefore(new JwtAuthnFilter(authnService), BasicAuthenticationFilter.class);
				        //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				        //.and().httpBasic();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        		
        return http.build();
    }
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
		log.info("ingnoring the security");
		return (web) -> web.ignoring().requestMatchers("/swagger-ui.html","/swagger-ui/**","/v3/api-docs/**");
    }
}
