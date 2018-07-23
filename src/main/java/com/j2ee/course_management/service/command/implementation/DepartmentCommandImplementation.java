package com.j2ee.course_management.service.command.implementation;

import com.j2ee.course_management.model.Department;
import com.j2ee.course_management.repository.DepartmentRepository;
import com.j2ee.course_management.service.command.DepartmentCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional
public class DepartmentCommandImplementation implements DepartmentCommand {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	public BindingResult createDepartment(Department department, BindingResult bindingResult) {
		if (this.departmentRepository.findByName(department.getName()) == null) {
			departmentRepository.save(department);
			return bindingResult;
		}
		bindingResult.rejectValue("name", "error.department","Conflict: Department already exist");
		return bindingResult;
	}

	@Override
	public BindingResult updateDepartment(Department newDpt, Long departmentId, BindingResult bindingResult) {
		Department oldDept = this.departmentRepository.getOne(departmentId);
		if (oldDept != null) {
			oldDept.setName(newDpt.getName());
			oldDept.setCourses(newDpt.getCourses());
			departmentRepository.save(oldDept);
			return bindingResult;
		}
		bindingResult.rejectValue("id", "error.department","Not Found: Department id does not exist");
		return bindingResult;
	}

	@Override
	public void deleteDepartment(Long departmentId) {
		this.departmentRepository.deleteById(departmentId);
	}
}
