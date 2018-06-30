package com.j2ee.course_management.service.command;

import com.j2ee.course_management.model.Course;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;

@PreAuthorize("")
public interface CourseCommand {

	Course createCourse(Course course);
	BindingResult updateCourse(Course course, Long courseId, BindingResult bindingResult);
	void deleteCourse(Long courseId);

}
