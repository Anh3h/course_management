package com.j2ee.course_management.dao.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.j2ee.course_management.dao.CourseDAO;
import com.j2ee.course_management.model.Course;
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
		String sql = "SELECT * FROM user_course INNER JOIN user ON user_course.user_id = user.id AND user_course.course_id=? AND user.role_id=2";
		try {
			return this.jdbcTemplate.query(sql, new Object[]{courseId}, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Course> getUnUsedCourses(Long departmentId, Long userId) {
		String sql = "SELECT * FROM course, department_course WHERE department_course.department_id = ? AND "
				+ "department_course.course_id = course.id AND course.id NOT IN (SELECT course_id FROM user_course WHERE user_id = ?)";
		try {
			return this.jdbcTemplate.query(sql, new Object[]{departmentId, userId}, new CourseMapper());
		} catch (EmptyResultDataAccessException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private static final class UserMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getLong("id"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setEmail(rs.getString("email"));
			user.setUsername(rs.getString("username"));
			user.setTelephone(rs.getString("telephone"));

			return user;
		}
	}

	private static final class CourseMapper implements RowMapper<Course> {

		@Override
		public Course mapRow(ResultSet rs, int rowNum) throws  SQLException {
			Course course = new Course();
			course.setId(rs.getLong("id"));
			course.setTitle(rs.getString("title"));
			course.setSemester(rs.getString("semester"));
			course.setCreditValue(rs.getString("credit_value"));
			course.setCode(rs.getString("code"));
			course.setOutline(rs.getString("outline"));

			return course;
		}
	}
}
