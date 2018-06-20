package com.j2ee.course_management.controller;

import com.j2ee.course_management.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("auth/login");
		return modelAndView;
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main/dashboard");
		return modelAndView;
	}

}
