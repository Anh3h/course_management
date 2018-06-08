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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseQuery courseQuery;

	@Autowired
	private CourseCommand courseCommand;

	@RequestMapping(
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Course> createCourse(@RequestBody Course course) {
		Course newCourse = this.courseCommand.createCourse(course);
		return new ResponseEntity<>(newCourse, HttpStatus.CREATED);
	}

	@RequestMapping(
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Page<Course>> getCourses(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
		page = pageAttributes.get("page");
		size = pageAttributes.get("size");

		Page<Course> courses = this.courseQuery.findAll(page, size);
		if (page > courses.getTotalPages()) {
			throw BadRequestException.create("Bad Request: Page number does not exist");
		}
		return new ResponseEntity<>(courses, HttpStatus.OK);
	}

	@RequestMapping(
			value = "/courseId",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Course> getCourse(@PathVariable("courseId") Long courseId) {
		Course course = this.courseQuery.findById(courseId);
		if (course == null) {
			throw NotFoundException.create("Not Found: Course with id, {0} does not exist", courseId);
		}
		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	@RequestMapping(
			value = "/code",
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
			value = "/courseId",
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
			value = "/courseId",
			method = RequestMethod.DELETE
	)
	public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("courseId") Long courseId) {
		this.courseCommand.deleteCourse(courseId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
