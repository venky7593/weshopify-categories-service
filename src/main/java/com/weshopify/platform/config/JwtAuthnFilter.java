package com.weshopify.platform.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class JwtAuthnFilter extends GenericFilterBean {
	
	
	private JwtAuthenticationService jwtAuthnService;
	
	JwtAuthnFilter(JwtAuthenticationService jwtAuthnService){
		this.jwtAuthnService = jwtAuthnService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Authentication authn =  jwtAuthnService.authenticateUser((HttpServletRequest) request);
		SecurityContextHolder.getContext().setAuthentication(authn);
		chain.doFilter(request, response);

	}

}
