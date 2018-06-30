package com.j2ee.course_management.service.command;

import com.j2ee.course_management.model.Department;
import org.springframework.validation.BindingResult;

public interface DepartmentCommand {

	Department createDepartment(Department department);
	BindingResult updateDepartment(Department department, Long departmentId, BindingResult bindingResult);
	void deleteDepartment(Long departmentId);

}
