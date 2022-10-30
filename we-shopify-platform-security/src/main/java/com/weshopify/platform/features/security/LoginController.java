package com.weshopify.platform.features.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.weshopify.platform.features.customers.CustomerBean;

@Controller
public class LoginController {
	Logger log = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public String loginCustomer(CustomerBean customer, Model model) {
		log.info("Customer Registration information is:\t");
		log.info(customer.toString());
		String userName = customer.getUserName();
		String password = customer.getPassword();
		boolean isLogin = StringUtils.hasText(userName) && StringUtils.hasText(password);
		if (isLogin) {
			model.addAttribute("principal", userName);
			return "dashboard.html";
		}
		model.addAttribute("message", isLogin);
		
		return "home.html";
	}
}
