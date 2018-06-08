package com.j2ee.course_management.service.command.implementation;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.ConflictException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.Role;
import com.j2ee.course_management.repository.RoleRepository;
import com.j2ee.course_management.service.command.RoleCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleCommandImplementation implements RoleCommand {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role createRole(Role role) {
		if (roleRepository.findByName(role.getName()) == null) {
			role.setId(0L);
			return roleRepository.save(role);
		}
		throw ConflictException.create("Conflict: Role, {0} already exist", role.getName());
	}

	@Override
	public Role updateRole(Role role) {
		if (role.getId() != null) {
			if (roleRepository.existsById(role.getId())) {
				return roleRepository.save(role);
			}
			throw NotFoundException.create("Not Found: Role id {0} does not exist", role.getId());
		}
		throw BadRequestException.create("Not Found: Role id cannot be null");
	}
}
