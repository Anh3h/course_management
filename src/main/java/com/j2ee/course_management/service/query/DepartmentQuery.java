package com.j2ee.course_management.service.query;

import java.util.List;

import com.j2ee.course_management.model.Department;
import org.springframework.data.domain.Page;

public interface DepartmentQuery {

	Department findById(Long id);
	Department findByName(String name);
	Page<Department> findAll(Integer pageNumber, Integer pageSize);

}
