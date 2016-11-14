package com.blackhole.fiman.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.blackhole.fiman.documents.User;
import com.blackhole.fiman.documents.UserRepository;

@Component
public class MongoAuthenticationProvider implements AuthenticationProvider {

	private final static Logger log = LoggerFactory.getLogger(MongoAuthenticationProvider.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("authenticate ...");
		
		String userName = authentication.getName();
		String userPassword = authentication.getCredentials() != null ? authentication.getCredentials().toString() : null;
		
		log.debug("Login [user: " + userName + ", pass: ********]");
		
		if(userName == null || userPassword == null) {
			return null;
		}
		
		User user = userRepository.findByUsername(userName);
		
		if(user == null) {
			return null;
		}
		
		if(SecurityHelper.authenticate(userPassword.toCharArray(), user.getPasswordHash())) {
			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			Authentication auth = new UsernamePasswordAuthenticationToken(userName, userPassword, grantedAuthorities);
			return auth;
		}
		
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
