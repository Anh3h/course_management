package com.j2ee.course_management.service.query.implementation;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import com.j2ee.course_management.dao.CourseDAO;
import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.properties.StorageProperties;
import com.j2ee.course_management.repository.CourseRepository;
import com.j2ee.course_management.repository.UserRepository;
import com.j2ee.course_management.service.query.CourseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
	private UserRepository userRepository;

	@Autowired
	private CourseDAO courseDAO;

	private final Path rootLocation;

	@Autowired
	public CourseQueryImplementation(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

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
	public List<Course> getUnUsedCourse(Long departmentId, String username) {
		User user;
		if (this.isEmail(username))
			user = this.userRepository.findByUsername(username);
		else
			user = this.userRepository.findByUsername(username);
		return courseDAO.getUnUsedCourses(departmentId, user.getId());
	}

	@Override
	public Resource downloadCourseOutline(Long courseId) {
		Course course = courseRepository.getOne(courseId);
		try {
			String dirURL = course.getId().toString() + "/";
			Path file = this.rootLocation.resolve(dirURL + course.getOutline());
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				System.out.println("Could not read file...  ");
			}
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Course findByTitle(String title) {
		return courseRepository.findByTitle(title);
	}

	@Override
	public Page<Course> findAll(Integer pageNumber, Integer pageSize) {
		return courseRepository.findAll(PageRequest.of(pageNumber-1, pageSize));
	}

	@Override
	public List<Course> findAll() {
		return courseRepository.findAll();
	}

	private boolean isEmail(String email)
	{
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
				"[a-zA-Z0-9_+&*-]+)*@" +
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
				"A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}
}
