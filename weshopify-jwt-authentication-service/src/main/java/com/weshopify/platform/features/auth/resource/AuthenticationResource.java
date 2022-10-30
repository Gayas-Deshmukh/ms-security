package com.weshopify.platform.features.auth.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.weshopify.platform.features.auth.exception.InvalidTokenException;
import com.weshopify.platform.features.auth.exception.TokenExpiredException;
import com.weshopify.platform.features.auth.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class AuthenticationResource 
{
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@GetMapping(value = "/authn")
	public Map<String, String> authenticate()
	{
		Authentication	authentication	= SecurityContextHolder.getContext().getAuthentication();
		
		String loggedinUser = authentication.getName();
		
		log.info("Loggedin User : " + loggedinUser);
		
		List<String>  rolelist	=	new ArrayList<>();
		
		Collection<? extends GrantedAuthority>	autorityList	=	authentication.getAuthorities();
		
		autorityList.forEach(grantedauth -> {
			String role	= grantedauth.getAuthority();
			rolelist.add(role);
		});
		
		String Jwttoken	=	jwtTokenService.createToken(loggedinUser, rolelist);
		// you can verify token here -> https://jwt.io/
		
		Map<String, String>	 jsonTokenMap	=	new HashMap<>();
		
		jsonTokenMap.put("AccessToken", Jwttoken);
		
		return jsonTokenMap;
	}
	
	@GetMapping(value = "/validate-token")
	public Map<String, Object> validateToken(@RequestHeader("Authorization") String tokenValue)
	{
		log.info("Autorization Token is : " + tokenValue);
		
		Map<String, Object> jsonTokenMap = new HashMap<>();
		
		try 
		{
			UserDetails userDetails = jwtTokenService.validateToken(tokenValue);
			
			log.info("Credentials Expired?:\t" + !userDetails.isCredentialsNonExpired());
			
			if (userDetails.isCredentialsNonExpired()) 
			{
				jsonTokenMap.put("isValidToken", true);
			}
		} 
		catch (TokenExpiredException e) 
		{
			jsonTokenMap.put("error", e.getLocalizedMessage());
			jsonTokenMap.put("isTokenExpired", true);
			jsonTokenMap.put("message", "Current Token expired!! Please get the new one");
		} 
		catch (InvalidTokenException e) 
		{
			jsonTokenMap.put("error", e.getLocalizedMessage());
			jsonTokenMap.put("isInvalidToken", true);
		}

		return jsonTokenMap;
	}
}
