package com.j2ee.course_management.service.command;

import com.j2ee.course_management.model.User;

public interface UserCommand {

	User createUser(User user);
	User updateUser(User user);

}
