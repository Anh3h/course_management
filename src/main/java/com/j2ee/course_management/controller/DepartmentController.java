package com.j2ee.course_management.controller;

import java.util.Map;

import com.j2ee.course_management.model.Department;
import com.j2ee.course_management.service.command.DepartmentCommand;
import com.j2ee.course_management.service.query.DepartmentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DepartmentController {

	@Autowired
	private DepartmentQuery departmentQuery;

	@Autowired
	private DepartmentCommand departmentCommand;

	/*@RequestMapping(
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Course> createCourse(@RequestBody Course course) {
		Course newCourse = this.courseCommand.createCourse(course);
		return new ResponseEntity<>(newCourse, HttpStatus.CREATED);
	}*/

	@RequestMapping(
			value = "/departments",
			method = RequestMethod.GET
	)
	public ModelAndView getDepartments(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		ModelAndView modelAndView = new ModelAndView();
		Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
		page = pageAttributes.get("page");
		size = pageAttributes.get("size");

		Page<Department> departments = this.departmentQuery.findAll(page, size);
		modelAndView.setViewName("option/list");
		modelAndView.addObject("departments", departments);
		return modelAndView;
	}

	@RequestMapping(
			value = "/departments/{departmentId}",
			method = RequestMethod.GET
	)
	public ModelAndView getDepartment(@PathVariable("departmentId") Long departmentId) {
		ModelAndView modelAndView = new ModelAndView();
		Department department = this.departmentQuery.findById(departmentId);
		modelAndView.addObject("department", department);
		modelAndView.setViewName("option/view");
		return modelAndView;
	}

	@RequestMapping(
			value = "/departments/edit/{departmentId}",
			method = RequestMethod.GET
	)
	public ModelAndView updateDepartmentForm(@PathVariable("departmentId") Long departmentId) {
		ModelAndView modelAndView = new ModelAndView();
		Department department = this.departmentQuery.findById(departmentId);
		modelAndView.addObject("department", department);
		modelAndView.setViewName("option/edit");
		return modelAndView;
	}

	@RequestMapping(
			value = "/departments/update/{departmentId}",
			method = RequestMethod.POST
	)
	public ModelAndView updateDepartments(@PathVariable("departmentId") Long departmentId, @ModelAttribute Department department,
			BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		bindingResult = this.departmentCommand.updateDepartment(department, departmentId, bindingResult);
		if (bindingResult.hasErrors()){
			modelAndView.setViewName("departments/edit");
		}
		modelAndView.setViewName("redirect:/departments/" + departmentId);
		return modelAndView;
	}

	/*@RequestMapping(
			value = "/{courseId}",
			method = RequestMethod.DELETE
	)
	public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("courseId") Long courseId) {
		this.courseCommand.deleteCourse(courseId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}*/

}
