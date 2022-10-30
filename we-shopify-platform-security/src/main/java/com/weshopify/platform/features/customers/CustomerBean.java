package com.weshopify.platform.features.customers;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weshopify.platform.features.customers.commons.EmailDomainValid;
import com.weshopify.platform.features.customers.commons.PasswordValidator;

import lombok.Data;

@Data
public class CustomerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3392255258797133035L;
	
	private int customerId;
	private boolean selfReg;
	
	//@NotEmpty(message = "First Name must be Provided, It shouldnt be empty")
	@NotEmpty
	private String firstName;
	
	@NotEmpty(message = "Last Name must be Provided, It shouldnt be empty")
	private String lastName;
	
	@NotEmpty(message = "User Name must be Provided, It shouldnt be empty")
	private String userName;
	
	@NotEmpty(message = "Email must be Provided, It shouldnt be empty")
	@Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	//@EmailDomainValid(message = "Email Domain is Not Valid. Please Enter the valid email address")
	private String email;
	
	@NotEmpty(message = "Password Must be Provided, It shouldnt be empty")
	@PasswordValidator(message = "Password should be 8 characters Length with the "
			+ "One Letter must be Capital and One Number Should Present")
	private String password;
	
	@Pattern(regexp = "^([+]\\d{2})?\\d{10}$",message = "Mobile Number should be of minimum 10 digits")
	private String mobileNumber;
	
	@JsonIgnore
	@Transient
	private String role;
	
}
