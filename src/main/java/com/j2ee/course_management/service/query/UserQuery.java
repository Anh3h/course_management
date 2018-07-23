package com.j2ee.course_management.service.query;

import com.j2ee.course_management.model.Role;
import com.j2ee.course_management.model.User;
import org.springframework.data.domain.Page;

public interface UserQuery {

	User findById(Long id);
	User findByEmail(String email);
	User findByUsername(String username);
	Role getUserRole(String username);
	Page<User> findAll(Integer pageNumber, Integer pageSize);

}
