package com.j2ee.course_management.dao;

import java.util.List;

import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.model.User;

public interface CourseDAO {

	List<User> getCourseLecturers( Long courseId );
	List<Course> getUnUsedCourses( Long departmentId, Long userId );

}
