package com.j2ee.course_management.service.command.implementation;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.ConflictException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.User;
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
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public BindingResult createUser(User user, BindingResult bindingResult) {
		if (userRepository.findByEmail(user.getEmail()) == null) {
			if (userRepository.findByEmail(user.getUsername()) == null) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setId(0L);
				userRepository.save(user);
				return bindingResult;
			}
			bindingResult.rejectValue("Username", "error.user",
					"Conflict: User username already exist");
			return bindingResult;
		}
		bindingResult.rejectValue("Email", "error.user",
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
		bindingResult.rejectValue("Id", "error.user","Not Found: User id does not exist");
		return bindingResult;
	}
}
