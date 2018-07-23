package com.j2ee.course_management.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Course {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	@Column(unique = true)
	private String code;
	@NotNull
	@Column(unique = true)
	private String title;
	@NotNull
	@Column(name = "credit_value")
	private String creditValue;
	private String outline;
	@NotNull
	private String semester;
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
	private Set<User> users;
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
	private Set<Department> departments;


	public Course() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(String creditValue) {
		this.creditValue = creditValue;
	}

	public String getOutline() {
		return outline;
	}

	public void setOutline(String outline) {
		this.outline = outline;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		this.users.add(user);
	}

	public void addUserList(Set<User> users) {
		this.users.addAll(users);
	}

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}
}
