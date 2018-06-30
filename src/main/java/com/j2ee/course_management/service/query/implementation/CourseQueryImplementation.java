package com.j2ee.course_management.service.query.implementation;

import java.util.List;

import com.j2ee.course_management.dao.CourseDAO;
import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.repository.CourseRepository;
import com.j2ee.course_management.service.query.CourseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourseQueryImplementation implements CourseQuery{

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseDAO courseDAO;

	@Override
	public Course findById(Long id) {
		return courseRepository.getOne(id);
	}

	@Override
	public Course findByCode(String code) {
		return courseRepository.findByCode(code);
	}

	@Override
	public List<User> getCourseLecturers(Long courseId) {
		return courseDAO.getCourseLecturers(courseId);
	}

	@Override
	public Course findByTitle(String title) {
		return courseRepository.findByTitle(title);
	}

	@Override
	public Page<Course> findAll(Integer pageNumber, Integer pageSize) {
		return courseRepository.findAll(PageRequest.of(pageNumber-1, pageSize));
	}
}
