package com.j2ee.course_management.service.command;

import com.j2ee.course_management.model.User;
import org.springframework.validation.BindingResult;

public interface UserCommand {

	BindingResult createUser(User user, BindingResult bindingResult);
	BindingResult updateUser(User user, Long userId, BindingResult bindingResult);
	BindingResult updateProfile(User user, String username, BindingResult bindingResult);
	void dropCourse(Long userId, Long courseId);

}
