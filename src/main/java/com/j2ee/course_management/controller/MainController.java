package com.j2ee.course_management.controller;

import java.util.List;

import com.j2ee.course_management.model.Role;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.service.command.UserCommand;
import com.j2ee.course_management.service.query.RoleQuery;
import com.j2ee.course_management.service.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private UserCommand userCommand;

	@Autowired
	private RoleQuery roleQuery;

	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("auth/login");
		return modelAndView;
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard(Authentication authentication){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.setViewName("main/dashboard");
		return modelAndView;
	}

	@RequestMapping(value = "/profile/view", method = RequestMethod.GET)
	public ModelAndView viewProfile(Authentication authentication){
		ModelAndView modelAndView = new ModelAndView();
		User user = this.userQuery.findByUsername(authentication.getName());
		modelAndView.addObject("user", user);
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.setViewName("profile/view");
		return modelAndView;
	}

	@RequestMapping(
			value = "/profile/edit",
			method = RequestMethod.GET
	)
	public ModelAndView getUpdateProfileForm(Authentication authentication) {
		ModelAndView modelAndView = new ModelAndView();
		User user = this.userQuery.findByUsername(authentication.getName());
		Page<Role> roles = this.roleQuery.findAll(1, 20);
		modelAndView.addObject("user", user);
		modelAndView.addObject("roles", roles);
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.setViewName("profile/edit");
		return modelAndView;
	}

	@RequestMapping(
			value = "/profile/edit",
			method = RequestMethod.POST
	)
	public ModelAndView updateProfile(Authentication authentication, @ModelAttribute User user,
			BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		bindingResult = this.userCommand.updateProfile(user, authentication.getName(), bindingResult);
		if (bindingResult.hasErrors()){
			modelAndView.setViewName("profile/edit");
		}
		modelAndView.setViewName("redirect:/profile/view");
		return modelAndView;
	}

	@RequestMapping(
			value = "/profile/courses",
			method = RequestMethod.GET
	)
	public ModelAndView getCourserByUserId(Authentication authentication) {
		ModelAndView modelAndView = new ModelAndView();
		User user = this.userQuery.findByUsername(authentication.getName());
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("user", user);
		modelAndView.setViewName("profile/courses");
		return modelAndView;
	}
}
