package com.j2ee.course_management.service.command.implementation;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.ConflictException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.repository.CourseRepository;
import com.j2ee.course_management.repository.RoleRepository;
import com.j2ee.course_management.repository.UserRepository;
import com.j2ee.course_management.service.command.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional
public class UserCommandImplementation implements UserCommand {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public BindingResult createUser(User user, BindingResult bindingResult) {
		if (userRepository.findByEmail(user.getEmail()) == null) {
			if (userRepository.findByEmail(user.getUsername()) == null) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				userRepository.save(user);
				return bindingResult;
			}
			bindingResult.rejectValue("username", "error.user",
					"Conflict: User username already exist");
			return bindingResult;
		}
		bindingResult.rejectValue("email", "error.user",
				"Conflict: User email already exist");
		return bindingResult;
	}

	@Override
	public BindingResult updateUser(User newUser, Long userId, BindingResult bindingResult) {
		User oldUser = this.userRepository.getOne(userId);
		if (oldUser != null) {
			oldUser.setFirstName(newUser.getFirstName());
			oldUser.setLastName(newUser.getLastName());
			oldUser.setEmail(newUser.getEmail());
			oldUser.setTelephone(newUser.getTelephone());
			userRepository.save(oldUser);
			return bindingResult;
		}
		bindingResult.rejectValue("id", "error.user","Not Found: User id does not exist");
		return bindingResult;
	}

	@Override
	public BindingResult updateProfile(User user, String username, BindingResult bindingResult) {
		User loggedInUser = this.userRepository.findByUsername(username);
		if (loggedInUser != null) {
			loggedInUser.setFirstName(user.getFirstName());
			loggedInUser.setLastName(user.getLastName());
			loggedInUser.setEmail(user.getEmail());
			loggedInUser.setTelephone(user.getTelephone());
			loggedInUser.setRole(user.getRole());
			userRepository.save(loggedInUser);
			return bindingResult;
		}
		bindingResult.rejectValue("username", "error.user","Not Found: Username does not exist");
		return bindingResult;
	}

	@Override
	public void dropCourse(Long userId, Long courseId) {
		User user = this.userRepository.getOne(userId);
		user.getCourses().remove(courseRepository.getOne(courseId));
		userRepository.save(user);
	}
}
