package com.flockinger.spongeblogSP;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpongeblogSpApplication {
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping("/secured")
	public String topSecret(){
		return "top secret";
	}
	
	@RequestMapping(value = "/")
	public void index(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui.html");
	}

	public static void main(String[] args) {
		SpringApplication.run(SpongeblogSpApplication.class, args);
	}
}
