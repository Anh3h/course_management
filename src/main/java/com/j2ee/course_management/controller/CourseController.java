package com.j2ee.course_management.controller;

import java.util.Map;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.ForbiddenException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.service.command.CourseCommand;
import com.j2ee.course_management.service.query.CourseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CourseController {

	@Autowired
	private CourseQuery courseQuery;

	@Autowired
	private CourseCommand courseCommand;

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
		modelAndView.addObject("course", course);
		modelAndView.setViewName("course/view");
		return modelAndView;
	}

	@RequestMapping(
			value = "/{code}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Course> getCourse(@PathVariable("code") String code) {
		Course course = this.courseQuery.findByCode(code);
		if (course == null) {
			throw NotFoundException.create("Not Found: Course with course code, {0} does not exist", code);
		}
		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	@RequestMapping(
			value = "/{courseId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Course> updateCourse(@RequestBody Course course, @PathVariable("courseId") Long courseId) {
		if (courseId == course.getId()) {
			Course updatedCourse = this.courseCommand.updateCourse(course);
			return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
		}
		throw ForbiddenException.create("Forbidden: Course id used in model does not match that on the path");
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
