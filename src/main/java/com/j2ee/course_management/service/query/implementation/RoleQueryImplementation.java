package com.j2ee.course_management.service.query.implementation;

import com.j2ee.course_management.model.Role;
import com.j2ee.course_management.repository.RoleRepository;
import com.j2ee.course_management.service.query.RoleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleQueryImplementation implements RoleQuery {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role findById(Long id) {
		return roleRepository.getOne(id);
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}

	@Override
	public Page<Role> findAll(Integer pageNumber, Integer pageSize) {
		return roleRepository.findAll(PageRequest.of(pageNumber-1, pageSize));
	}
}
