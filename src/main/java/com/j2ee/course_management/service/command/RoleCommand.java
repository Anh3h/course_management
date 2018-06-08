package com.j2ee.course_management.service.command;

import com.j2ee.course_management.model.Role;

public interface RoleCommand {

	Role createRole(Role role);
	Role updateRole(Role role);

}
