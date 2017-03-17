package com.flockinger.spongeblogSP.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlubController {

	@RequestMapping(path="/blub",method=RequestMethod.GET)
	public String getBlub(){
		return "blub blub";
	}
}
