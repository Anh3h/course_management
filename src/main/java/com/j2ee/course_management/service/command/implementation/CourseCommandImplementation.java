package com.j2ee.course_management.service.command.implementation;

import com.j2ee.course_management.dao.CourseDAO;
import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.ConflictException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.repository.CourseRepository;
import com.j2ee.course_management.service.command.CourseCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional
public class CourseCommandImplementation implements CourseCommand {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseDAO courseDAO;

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
	public BindingResult updateCourse(Course newCourse, Long courseId, BindingResult bindingResult) {
		Course oldCourse = this.courseRepository.getOne(courseId);
		if (oldCourse != null) {
			oldCourse.setCode(newCourse.getCode());
			oldCourse.setCreditValue(newCourse.getCreditValue());
			oldCourse.setDepartments(newCourse.getDepartments());
			oldCourse.setSemester(newCourse.getSemester());
			oldCourse.setTitle(newCourse.getTitle());
			oldCourse.setUsers(this.courseDAO.getCourseLecturers(courseId));
			System.out.println(newCourse.getUsers().size());
			oldCourse.addUserList(newCourse.getUsers());
			System.out.println(newCourse.getDepartments().get(0).getName());
			courseRepository.save(oldCourse);
			return bindingResult;
		}
		bindingResult.rejectValue("Id", "error.course","Not Found: Course id does not exist");
		return bindingResult;
	}

	@Override
	public void deleteCourse(Long courseId) {
		Course course = courseRepository.getOne(courseId);
		if (course != null){
			courseRepository.delete(course);
		}
	}
}
