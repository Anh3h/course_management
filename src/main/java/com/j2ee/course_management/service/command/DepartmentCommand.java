package com.j2ee.course_management.service.command;

import com.j2ee.course_management.model.Department;
import org.springframework.validation.BindingResult;

public interface DepartmentCommand {

	BindingResult createDepartment(Department department, BindingResult bindingResult);
	BindingResult updateDepartment(Department department, Long departmentId, BindingResult bindingResult);
	void deleteDepartment(Long departmentId);

}
