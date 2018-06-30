package com.j2ee.course_management.dao.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.j2ee.course_management.dao.CourseDAO;
import com.j2ee.course_management.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CourseDAOImpl implements CourseDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<User> getCourseLecturers(Long courseId) {
		String sql = "SELECT * FROM user, course, user_course WHERE course.id = ? AND user.role_id = 2";
		try {
			return this.jdbcTemplate.query(sql, new Object[]{courseId}, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public List<User> getCourseStudents(Long courseId) {
		String sql = "SELECT * FROM user, course, user_course WHERE course.id = ? AND user.role_id = 3";
		try {
			return this.jdbcTemplate.query(sql, new Object[]{courseId}, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private static final class UserMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setEmail(rs.getString("email"));
			user.setUsername(rs.getString("username"));
			user.setTelephone(rs.getString("telephone"));

			return user;
		}
	}
}
