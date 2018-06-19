package com.j2ee.course_management.service.query.implementation;

import com.j2ee.course_management.model.User;
import com.j2ee.course_management.repository.UserRepository;
import com.j2ee.course_management.service.query.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserQueryImplementation implements UserQuery {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User findById(Long id) {
		return userRepository.getOne(id);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Page<User> findAll(Integer pageNumber, Integer pageSize) {
		return userRepository.findAll(PageRequest.of(pageNumber-1, pageSize));
	}
}
