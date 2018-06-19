package com.j2ee.course_management.service.command;

import com.j2ee.course_management.model.Course;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("")
public interface CourseCommand {

	Course createCourse(Course course);
	Course updateCourse(Course course);
	void deleteCourse(Long courseId);

}
