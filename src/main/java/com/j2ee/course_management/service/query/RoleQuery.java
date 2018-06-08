package com.j2ee.course_management.service.query;

import com.j2ee.course_management.model.Role;
import org.springframework.data.domain.Page;

public interface RoleQuery {

	Role findById(Long id);
	Role findByName(String name);
	Page<Role> findAll(Integer pageNumber, Integer pageSize);


}
