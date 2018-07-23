package com.j2ee.course_management.service.command;

import com.j2ee.course_management.model.Course;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("")
public interface CourseCommand {

	BindingResult createCourse(Course course, BindingResult bindingResult);
	BindingResult updateCourse(Course course, Long courseId, BindingResult bindingResult);
	void takeCourse(String username, Long courseId);
	void uploadCourseOutline(MultipartFile file, Long courseId);
	void deleteCourseOutline(Long courseId);
	void deleteCourse(Long courseId);

}
