package pt.darkalpha.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
	
	@PostMapping("/login")
	public void logIn () {}

	@PostMapping("/logout")
	public void logOut () {}
	
}
