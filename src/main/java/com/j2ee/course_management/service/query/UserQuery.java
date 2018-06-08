package com.j2ee.course_management.service.query;

import com.j2ee.course_management.model.User;
import org.springframework.data.domain.Page;

public interface UserQuery {

	User findById(Long id);
	User findByEmail(String email);
	Page<User> findAll(Integer pageNumber, Integer pageSize);

}
