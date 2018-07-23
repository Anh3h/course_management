package com.j2ee.course_management.service.command.implementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.j2ee.course_management.dao.CourseDAO;
import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.ConflictException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.Course;
import com.j2ee.course_management.model.Department;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.properties.StorageProperties;
import com.j2ee.course_management.repository.CourseRepository;
import com.j2ee.course_management.repository.DepartmentRepository;
import com.j2ee.course_management.repository.UserRepository;
import com.j2ee.course_management.service.command.CourseCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class CourseCommandImplementation implements CourseCommand {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private UserRepository userRepository;

	private final Path rootLocation;

	@Autowired
	public CourseCommandImplementation(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public BindingResult createCourse(Course course, BindingResult bindingResult) {
		if (courseRepository.findByCode(course.getCode()) == null) {
			if (courseRepository.findByTitle(course.getTitle()) == null) {
				course.setId(0L);
				course = courseRepository.save(course);
				setDefaultCouseOutline(course);
				return bindingResult;
			}
			bindingResult.rejectValue("title", "error.course","Conflict: Course title already exist");
			return bindingResult;
		}

		bindingResult.rejectValue("code", "error.course","Conflict: Course code already exist");
		return bindingResult;
	}

	private void setDefaultCouseOutline(Course course) {
		try {
			String filename = StringUtils.cleanPath("Lesson_plan.pdf");
			String dirURL = course.getId().toString() + "/";
			if (Files.notExists(this.rootLocation.resolve(dirURL))) {
				Files.createDirectories(this.rootLocation.resolve(dirURL));
			}
			Files.createFile(this.rootLocation.resolve(dirURL + filename));
			Files.copy(this.rootLocation.resolve(filename), this.rootLocation.resolve(dirURL + filename),
					StandardCopyOption.REPLACE_EXISTING);
			course.setOutline(filename);
			courseRepository.save(course);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public BindingResult updateCourse(Course newCourse, Long courseId, BindingResult bindingResult) {
		Course oldCourse = this.courseRepository.getOne(courseId);
		if (oldCourse != null) {
			oldCourse.setCode(newCourse.getCode());
			oldCourse.setTitle(newCourse.getTitle());
			oldCourse.setCreditValue(newCourse.getCreditValue());
			oldCourse.setSemester(newCourse.getSemester());
			courseRepository.save(oldCourse);
			return bindingResult;
		}
		bindingResult.rejectValue("id", "error.course","Not Found: Course id does not exist");
		return bindingResult;
	}

	@Override
	public void takeCourse(String username, Long courseId) {
		User user;
		if (this.isEmail(username))
			user = this.userRepository.findByUsername(username);
		else
			user = this.userRepository.findByUsername(username);
		user.addCourse(courseRepository.getOne(courseId));
		userRepository.save(user);
	}

	@Override
	public void uploadCourseOutline(MultipartFile file, Long courseId) {
		Course course = courseRepository.getOne(courseId);
		try {
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			String dirURL = course.getId().toString() + "/";
			if (Files.notExists(this.rootLocation.resolve(dirURL))) {
				Files.createDirectories(this.rootLocation.resolve(dirURL));
			}
			Files.createFile(this.rootLocation.resolve(dirURL + filename));
			Files.copy(file.getInputStream(), this.rootLocation.resolve(dirURL + filename),
					StandardCopyOption.REPLACE_EXISTING);
			course.setOutline(filename);
			courseRepository.save(course);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteCourseOutline(Long courseId) {

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

	@Override
	public void deleteCourse(Long courseId) {
		courseRepository.deleteById(courseId);
	}
}
