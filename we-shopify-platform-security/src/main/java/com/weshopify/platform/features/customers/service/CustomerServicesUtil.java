package com.weshopify.platform.features.customers.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weshopify.platform.features.customers.commons.EmailDomainStatus;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServicesUtil {

	//private static final Logger log = LoggerFactory.getLogger(CustomerServicesUtil.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${email.domain.validate.api.value}")
	private String emailValidateApi;

	@Value("${email.domain.validate.api.key}")
	private String emailValidateApiKey;

	public boolean isValidEmailDomina(String email) {
		boolean isValidEmailDomain = false;

		log.info("email API Is:\t,{},{}", emailValidateApi, emailValidateApiKey);

		emailValidateApi = emailValidateApi + emailValidateApiKey + "&email=" + email;
		ResponseEntity<String> respEntity = restTemplate.getForEntity(emailValidateApi, String.class);
		if (respEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
			String emailDeliveryData = respEntity.getBody();
			JSONObject jsonData = new JSONObject(emailDeliveryData);
			if (jsonData.has("deliverability")) {
				String deliveryStatus = jsonData.getString("deliverability");
				log.info("email:\t {} is deliverable:\t {}",email,deliveryStatus);
				if(EmailDomainStatus.DELIVERABLE.getEmailDeliveryStatus().equals(deliveryStatus)) {
					isValidEmailDomain = true;
				}
			}
			
		}

		log.info("Is Valid Email Domain:\t {}", isValidEmailDomain);
		return isValidEmailDomain;
	}
}
