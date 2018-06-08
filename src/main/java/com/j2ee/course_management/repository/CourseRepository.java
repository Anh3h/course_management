package com.j2ee.course_management.repository;

import com.j2ee.course_management.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

	Course findByCode(@Param("code") String code);
	Course findByTitle(@Param("title") String title);

}
