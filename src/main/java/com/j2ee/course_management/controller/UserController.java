package com.j2ee.course_management.controller;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.model.Role;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.service.command.UserCommand;
import com.j2ee.course_management.service.query.RoleQuery;
import com.j2ee.course_management.service.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private RoleQuery roleQuery;

	@Autowired
	private UserCommand userCommand;

	@RequestMapping(
			value = "/users/create",
			method = RequestMethod.POST
	)
	public ModelAndView createUser(@ModelAttribute User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		bindingResult = this.userCommand.createUser(user, bindingResult);
		if (bindingResult.hasErrors()){
			modelAndView.setViewName("user/create");
		}
		modelAndView.setViewName("redirect:/users/");
		return modelAndView;
	}


	@RequestMapping(
			value = "/users/create",
			method = RequestMethod.GET
	)
	public ModelAndView createUserForm() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		Page<Role> roles = this.roleQuery.findAll(1, 10);
		modelAndView.addObject("user", user);
		modelAndView.addObject("roles", roles);
		modelAndView.setViewName("user/create");
		return modelAndView;
	}

	@RequestMapping(
			value = "/users",
			method = RequestMethod.GET
	)
	public ModelAndView getUsers(Integer page, Integer size) {
		ModelAndView modelAndView = new ModelAndView();
		Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
		page = pageAttributes.get("page");
		size = pageAttributes.get("size");

		Page<User> users = this.userQuery.findAll(page, size);
		modelAndView.setViewName("user/list");
		modelAndView.addObject("users", users);
		return modelAndView;
	}

	@RequestMapping(
			value = "/users/{userId}",
			method = RequestMethod.GET
	)
	public ModelAndView getUserById(@PathVariable("userId") Long userId) {
		ModelAndView modelAndView = new ModelAndView();
		User user = this.userQuery.findById(userId);
		modelAndView.addObject("user", user);
		modelAndView.setViewName("user/view");
		return modelAndView;
	}

	@RequestMapping(
			value = "/users/edit/{userId}",
			method = RequestMethod.GET
	)
	public ModelAndView updateUserForm(@PathVariable("userId") Long userId) {
		ModelAndView modelAndView = new ModelAndView();
		User user = this.userQuery.findById(userId);
		modelAndView.addObject("user", user);
		modelAndView.setViewName("user/edit");
		return modelAndView;
	}

	@RequestMapping(
			value = "/users/update/{userId}",
			method = RequestMethod.POST
	)
	public ModelAndView updateUser(@PathVariable("userId") Long userId, @ModelAttribute User user,
			BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		bindingResult = this.userCommand.updateUser(user, userId, bindingResult);
		if (bindingResult.hasErrors()){
			modelAndView.setViewName("user/edit");
		}
		modelAndView.setViewName("redirect:/users/" + userId);
		return modelAndView;
	}

}
