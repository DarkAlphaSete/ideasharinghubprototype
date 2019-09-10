package pt.darkalpha.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	@GetMapping("/")
	public String getAppPage() {
		return "app";
	}
	
	@GetMapping("/admin")
	public String getAdminPage() {
		return "admin";
	}
	
	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}

}
