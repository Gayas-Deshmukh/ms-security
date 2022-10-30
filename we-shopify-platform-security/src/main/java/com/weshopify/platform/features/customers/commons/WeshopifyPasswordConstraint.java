package com.weshopify.platform.features.customers.commons;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeshopifyPasswordConstraint implements ConstraintValidator<PasswordValidator, String> {
	
	private static final Logger log = LoggerFactory.getLogger(WeshopifyPasswordConstraint.class);
	
	final String PASSWORD_REG_EX = "^.*(?=.{8,})((?=.*[!@#$%^&*()\\-_=+{};:,<.>]){1})(?=.*\\d)((?=.*[a-z]){1})((?=.*[A-Z]){1}).*$"; 
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		log.info("The Password entered is:\t"+value);
		Pattern passwordPattern = Pattern.compile(PASSWORD_REG_EX);
		boolean isPasswordValid = passwordPattern.matcher(value).find();
		log.info("does Password match with RegEx:\t"+isPasswordValid);
		return isPasswordValid;
	}

}
