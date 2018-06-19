package com.j2ee.course_management.service.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.exception.NotFoundException;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public class CustomUserDetailService implements UserDetailsService {

	private UserRepository userRepository;
	final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public CustomUserDetailService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;

	}

	@Override
	public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {

		if (isEmail(string)) {
			User user = userRepository.findByEmail(string);
			if (user == null) {
				throw NotFoundException.create("Not Found: Email {0} does not exist", string);
			}
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(user.getRole());
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					true, true, true, true, authorities);
		}
		User user = userRepository.findByUsername(string);
		if (user == null) {
			throw new UsernameNotFoundException(string);
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(user.getRole());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				true, true, true, true, authorities);
	}

	private boolean isEmail(String email)
	{
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
				"[a-zA-Z0-9_+&*-]+)*@" +
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
				"A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

}
