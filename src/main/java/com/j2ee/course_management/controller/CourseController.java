package com.j2ee.course_management.controller;

import java.util.List;
import java.util.Map;

import com.j2ee.course_management.exception.ForbiddenException;
import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.model.Department;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.service.command.CourseCommand;
import com.j2ee.course_management.service.query.CourseQuery;
import com.j2ee.course_management.service.query.DepartmentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CourseController {

	@Autowired
	private CourseQuery courseQuery;

	@Autowired
	private CourseCommand courseCommand;

	@Autowired
	private DepartmentQuery departmentQuery;

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
			value = "/courses",
			method = RequestMethod.GET
	)
	public ModelAndView getCourses(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		ModelAndView modelAndView = new ModelAndView();
		Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
		page = pageAttributes.get("page");
		size = pageAttributes.get("size");

		Page<Course> courses = this.courseQuery.findAll(page, size);
		modelAndView.setViewName("course/list");
		modelAndView.addObject("courses", courses);
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/{courseId}",
			method = RequestMethod.GET
	)
	public ModelAndView getCourse(@PathVariable("courseId") Long courseId) {
		ModelAndView modelAndView = new ModelAndView();
		Course course = this.courseQuery.findById(courseId);
		List<User> lecturers = this.courseQuery.getCourseLecturers(courseId);
		modelAndView.addObject("course", course);
		modelAndView.addObject("lecturers", lecturers);
		modelAndView.setViewName("course/view");
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/edit/{courseId}",
			method = RequestMethod.GET
	)
	public ModelAndView updateCourseForm(@PathVariable("courseId") Long courseId) {
		ModelAndView modelAndView = new ModelAndView();
		Course course = this.courseQuery.findById(courseId);
		Page<Department> departments = this.departmentQuery.findAll(1, 20);
		List<User> users = this.courseQuery.getCourseLecturers(courseId);
		modelAndView.addObject("course", course);
		modelAndView.addObject("departments", departments);
		modelAndView.addObject("users", users);
		modelAndView.setViewName("course/edit");
		return modelAndView;
	}

	@RequestMapping(
			value = "/courses/update/{courseId}",
			method = RequestMethod.POST
	)
	public ModelAndView updateCourse(@PathVariable("courseId") Long courseId, @ModelAttribute Course course,
			BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		bindingResult = this.courseCommand.updateCourse(course, courseId, bindingResult);
		if (bindingResult.hasErrors()){
			modelAndView.setViewName("courses/edit");
		}
		modelAndView.setViewName("redirect:/courses/" + courseId);
		return modelAndView;
	}

	@RequestMapping(
			value = "/{courseId}",
			method = RequestMethod.DELETE
	)
	public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("courseId") Long courseId) {
		this.courseCommand.deleteCourse(courseId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
