package com.j2ee.course_management.service.query.implementation;

import java.util.List;

import com.j2ee.course_management.model.Department;
import com.j2ee.course_management.repository.DepartmentRepository;
import com.j2ee.course_management.service.query.DepartmentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepartmentQueryImplementation implements DepartmentQuery {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	public Department findById(Long id) {
		return departmentRepository.getOne(id);
	}

	@Override
	public Department findByName(String name) {
		return departmentRepository.findByName(name);
	}

	@Override
	public Page<Department> findAll(Integer pageNumber, Integer pageSize) {
		return departmentRepository.findAll(PageRequest.of(pageNumber-1, pageSize));
	}

}
