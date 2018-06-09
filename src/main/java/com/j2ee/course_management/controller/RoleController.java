package com.j2ee.course_management.controller;

import java.util.Map;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.ForbiddenException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.Role;
import com.j2ee.course_management.service.command.RoleCommand;
import com.j2ee.course_management.service.query.RoleQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private RoleQuery roleQuery;

	@Autowired
	private RoleCommand roleCommand;

	@ApiOperation(value="Add new role")
	@RequestMapping(
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Role> createRole(@RequestBody Role role) {
		Role newRole = this.roleCommand.createRole(role);
		return new ResponseEntity<>(newRole, HttpStatus.CREATED);
	}

	@ApiOperation(value="Get all/some roles")
	@RequestMapping(
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Page<Role>> getRoles(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
		page = pageAttributes.get("page");
		size = pageAttributes.get("size");

		Page<Role> roles = this.roleQuery.findAll(page, size);
		if (page > roles.getTotalPages()) {
			throw BadRequestException.create("Bad Request: Page number does not exist");
		}
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}

	@ApiOperation(value="Find role by role_id")
	@RequestMapping(
			value = "/roleId",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Role> getRole(@PathVariable("roleId") Long roleId) {
		Role role = this.roleQuery.findById(roleId);
		if (role == null) {
			throw NotFoundException.create("Not Found: Role with id, {0} does not exist", roleId);
		}
		return new ResponseEntity<>(role, HttpStatus.OK);
	}

	@ApiOperation(value="Update existing role")
	@RequestMapping(
			value = "/roleId",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Role> updateRole(@RequestBody Role role, @PathVariable("roleId") Long roleId) {
		if (roleId == role.getId()) {
			Role updatedRole = this.roleCommand.updateRole(role);
			return new ResponseEntity<>(updatedRole, HttpStatus.OK);
		}
		throw ForbiddenException.create("Forbidden: Role id used in model does not match that on the path");
	}

}
