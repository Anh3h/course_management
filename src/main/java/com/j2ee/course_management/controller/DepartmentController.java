package com.j2ee.course_management.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.model.Department;
import com.j2ee.course_management.service.command.CourseCommand;
import com.j2ee.course_management.service.command.DepartmentCommand;
import com.j2ee.course_management.service.query.CourseQuery;
import com.j2ee.course_management.service.query.DepartmentQuery;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DepartmentController {

	@Autowired
	private DepartmentQuery departmentQuery;

	@Autowired
	private CourseQuery courseQuery;

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private CourseCommand courseCommand;

	@Autowired
	private DepartmentCommand departmentCommand;

	@RequestMapping(
			value = "/departments/create",
			method = RequestMethod.POST
	)
	public ModelAndView createDepartment(@ModelAttribute Department department, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		bindingResult = this.departmentCommand.createDepartment(department, bindingResult);
		if (bindingResult.hasErrors()){
			modelAndView.setViewName("departments/create");
		}
		modelAndView.setViewName("redirect:/departments/");
		return modelAndView;
	}

	@RequestMapping(
			value = "/departments/{departmentId}/courses/{courseId}",
			method = RequestMethod.GET
	)
	public ModelAndView takeCourse(Authentication authentication, @PathVariable("departmentId") Long departmentId, @PathVariable("courseId") Long courseId) {
		ModelAndView modelAndView = new ModelAndView();
		this.courseCommand.takeCourse(authentication.getName(), courseId);
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.setViewName("redirect:/departments/" + departmentId + "/courses");
		return modelAndView;
	}

	@RequestMapping(
			value = "/departments/create",
			method = RequestMethod.GET
	)
	public ModelAndView createDepartmentForm(Authentication authentication) {
		ModelAndView modelAndView = new ModelAndView();
		Department department = new Department();
		List<Course> courses = this.courseQuery.findAll();
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("department", department);
		modelAndView.addObject("courses", courses);
		modelAndView.setViewName("option/create");
		return modelAndView;
	}

	@RequestMapping(
			value = "/departments",
			method = RequestMethod.GET
	)
	public ModelAndView getDepartments(Authentication authentication, @RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		ModelAndView modelAndView = new ModelAndView();
		Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
		page = pageAttributes.get("page");
		size = pageAttributes.get("size");

		Page<Department> departments = this.departmentQuery.findAll(page, size);
		modelAndView.setViewName("option/list");
		modelAndView.addObject("departments", departments);
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		return modelAndView;
	}

	@RequestMapping(
			value = "/departments/{departmentId}",
			method = RequestMethod.GET
	)
	public ModelAndView getDepartment(Authentication authentication, @PathVariable("departmentId") Long departmentId) {
		ModelAndView modelAndView = new ModelAndView();
		Department department = this.departmentQuery.findById(departmentId);
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("department", department);
		modelAndView.setViewName("option/view");
		return modelAndView;
	}

	@RequestMapping(
			value = "/departments/{departmentId}/courses",
			method = RequestMethod.GET
	)
	public ModelAndView getCourseByDepartment(Authentication authentication, @PathVariable("departmentId") Long departmentId) {
		ModelAndView modelAndView = new ModelAndView();
		Department department = this.departmentQuery.findById(departmentId);
		List<Course> courses = this.courseQuery.getUnUsedCourse(departmentId, authentication.getName());
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("department", department);
		modelAndView.addObject("courses", courses);
		modelAndView.setViewName("option/courses");
		return modelAndView;
	}

	@RequestMapping(
			value = "/departments/edit/{departmentId}",
			method = RequestMethod.GET
	)
	public ModelAndView updateDepartmentForm( Authentication authentication, @PathVariable("departmentId") Long departmentId) {
		ModelAndView modelAndView = new ModelAndView();
		Department department = this.departmentQuery.findById(departmentId);
		List<Course> courses = this.courseQuery.findAll();
		modelAndView.addObject("role", authentication.getAuthorities().iterator().next().getAuthority());
		modelAndView.addObject("department", department);
		modelAndView.addObject("courses", courses);
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
