package com.j2ee.course_management.repository;

import com.j2ee.course_management.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	Department findByName(@Param("name") String name);

}
