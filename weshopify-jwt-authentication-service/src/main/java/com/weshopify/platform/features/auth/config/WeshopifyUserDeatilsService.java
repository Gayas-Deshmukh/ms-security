package com.weshopify.platform.features.auth.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service; 
import com.weshopify.platform.features.auth.models.Customer;
import com.weshopify.platform.features.auth.repository.CustomerDataRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeshopifyUserDeatilsService  implements UserDetailsService
{

	@Autowired
	private CustomerDataRepo customerDataRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		log.info("Loggedin User :" + username);
		
		Customer customer  = customerDataRepo.searchByEmail(username).get(0);
		
		String loggedInUser	=	customer.getEmail();
		log.info("User Fetched from DB : "+ loggedInUser);
		
		String passsword	=	customer.getPassword();
		log.info("Password Fetched from DB : "+ passsword);
		
		String roleName	=	customer.getRole().getRole();
		log.info("Role Name of Customer : "+ roleName);
		
		List<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority(roleName));
		
		return new User(username, passsword, authorityList);
	}
}
