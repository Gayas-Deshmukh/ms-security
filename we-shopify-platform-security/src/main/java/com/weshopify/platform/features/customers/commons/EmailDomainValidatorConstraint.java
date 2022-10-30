package com.weshopify.platform.features.customers.commons;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.weshopify.platform.features.customers.service.CustomerServicesUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailDomainValidatorConstraint implements ConstraintValidator<EmailDomainValid, String> {

	@Autowired
	private CustomerServicesUtil customerServicesUtil;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		log.info("inside the email domainn Validate method");
		boolean isValidEmailDomain = false;
		if(StringUtils.hasText(value)) {
			isValidEmailDomain = customerServicesUtil.isValidEmailDomina(value);
		}
		
		return isValidEmailDomain;
	}

}
