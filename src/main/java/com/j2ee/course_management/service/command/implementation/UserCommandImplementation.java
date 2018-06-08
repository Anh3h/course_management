package com.j2ee.course_management.service.command.implementation;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.ConflictException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.repository.UserRepository;
import com.j2ee.course_management.service.command.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCommandImplementation implements UserCommand {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User createUser(User user) {
		if (userRepository.findByEmail(user.getEmail()) == null) {
			if (userRepository.findByEmail(user.getUsername()) == null) {

				// TODO Add password encode before save password
				user.setId(0L);
				return userRepository.save(user);
			}
			throw ConflictException.create("Conflict: User username, {0} already exist", user.getUsername());
		}
		throw ConflictException.create("Conflict: User email, {0} already exist", user.getEmail());
	}

	@Override
	public User updateUser(User user) {
		if (user.getId() != null) {
			if (userRepository.existsById(user.getId())){
				return userRepository.save(user);
			}
			throw NotFoundException.create("Not Found: User id {0} does not exist", user.getId());
		}
		throw BadRequestException.create("Not Found: User id cannot be null");
	}
}
