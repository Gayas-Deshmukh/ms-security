package com.weshopify.platform.features.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping(value = {"","/","home"})
	public String renderHomePage() {
		System.out.println("i am in home controller");
		return "home.html";
	}
}
