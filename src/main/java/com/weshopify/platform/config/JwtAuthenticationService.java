package com.weshopify.platform.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weshopify.platform.model.Role;
import com.weshopify.platform.model.WSO2User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationService {

	private RedisTemplate<String, String> redisTemplate;
	HashOperations<String, String, String> hashOps = null;

	@Autowired
	private ObjectMapper mapper;

	private static final String JWT_TOKEN_HEADER_NAME = "Authorization";
	private static final String JWT_TOKEN_TYPE = "Bearer ";
	private static final String JWT_TOKEN_EXPIRY_KEY = "tokenExpiry";
	private static final String USER_ROLES_KEY = "USER_ROLES";
	private static final String USER_SUBJECT_NAME = "SUBJECT";

	JwtAuthenticationService(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		hashOps = redisTemplate.opsForHash();
	}

	public Authentication authenticateUser(HttpServletRequest request) {
		Authentication authn = null;
		String token = resolveToken(request);
		boolean isTokenValid = validateToken(token);
		if (isTokenValid) {

			Set<String> hkset = hashOps.keys(token);
			List<GrantedAuthority> roles = new ArrayList<>();

			Iterator<String> it = hkset.iterator();
			while (it.hasNext()) {
				String randomHash = it.next();
				String wso2UserData = hashOps.get(token, randomHash);
				try {
					WSO2User wso2User = mapper.readValue(wso2UserData, WSO2User.class);
					List<Role> rolesList = wso2User.getRoles();
					for (Role role : rolesList) {
						if ("default".equals(role.getType())) {
							String[] userRoles = role.getValue().split("\\,");
							for (String userRole : userRoles) {
								if (!userRole.equals("Internal/everyone")) {
									userRole = userRole.replace("Application/", "");
									log.info("provisioned user role is:\t" + userRole);
									roles.add(new SimpleGrantedAuthority(userRole));
								} else {
									log.info("skipping the Internal/evyone role");
								}
							}

						} else {
							log.info("skipping the non default roles");
						}
					}
					String userName = wso2User.getUserName();

					authn = new UsernamePasswordAuthenticationToken(userName, null, roles);
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return authn;
	}

	private boolean validateToken(String token) {
		if (hashOps.hasKey(JWT_TOKEN_EXPIRY_KEY, token)) {
			String expiryInSeconds = hashOps.get(JWT_TOKEN_EXPIRY_KEY, token);
			long tokenExpiryInSeconds = Long.valueOf(expiryInSeconds);
			if (expiryDate(tokenExpiryInSeconds).before(new Date())) {
				return false;
			}
			return true;
		} else {
			throw new RuntimeException("Token is Invalid!! Please Login Again");
		}

	}

	private String resolveToken(HttpServletRequest request) {
		String headerValue = request.getHeader(JWT_TOKEN_HEADER_NAME);
		if(StringUtils.hasText(headerValue)) {
			headerValue = headerValue.replace(JWT_TOKEN_TYPE, "");
			log.info("jwt token is {}", headerValue);
			return headerValue;
		}else {
			throw Unauthorized.create("No Token Provided", HttpStatus.FORBIDDEN, "Forbidden", null, null, null);
		}
		
	}

	private Date expiryDate(long tokenExpiry) {
		Date date = new Date();
		System.out.println(date);
		long time = date.getTime() + tokenExpiry * 1000;
		Date updatedDate = new Date(time);
		return updatedDate;
	}
}
