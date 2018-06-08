package com.j2ee.course_management.service.command.implementation;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.ConflictException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.repository.CourseRepository;
import com.j2ee.course_management.service.command.CourseCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourseCommandImplementation implements CourseCommand {

	@Autowired
	private CourseRepository courseRepository;

	@Override
	public Course createCourse(Course course) {
		if (courseRepository.findByCode(course.getCode()) == null) {
			if (courseRepository.findByTitle(course.getTitle()) == null) {
				course.setId(0L);
				return courseRepository.save(course);
			}
			throw ConflictException.create("Conflict: Course, {0} already exist", course.getTitle());
		}
		throw ConflictException.create("Conflict: Course, {0} already exist", course.getCode());
	}

	@Override
	public Course updateCourse(Course course) {
		if (course.getId() != null) {
			if (courseRepository.existsById(course.getId())) {
				return courseRepository.save(course);
			}
			throw NotFoundException.create("Not Found: Course id {0} does not exist", course.getId());
		}
		throw BadRequestException.create("Not Found: Course id cannot be null");
	}
}
