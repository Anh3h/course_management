package com.j2ee.course_management.service.query;

import com.j2ee.course_management.model.Course;
import org.springframework.data.domain.Page;

public interface CourseQuery {

	Course findById(Long id);
	Course findByCode(String code);
	Course findByTitle(String title);
	Page<Course> findAll(Integer pageNumber, Integer pageSize);

}
