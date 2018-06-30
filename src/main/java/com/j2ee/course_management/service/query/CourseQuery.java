package com.j2ee.course_management.service.query;

import java.util.List;

import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.model.User;
import org.springframework.data.domain.Page;

public interface CourseQuery {

	Course findById(Long id);
	Course findByCode(String code);
	List<User> getCourseLecturers(Long courseId);
	Course findByTitle(String title);
	Page<Course> findAll(Integer pageNumber, Integer pageSize);

}
