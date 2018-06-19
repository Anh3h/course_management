package com.j2ee.course_management.service.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.j2ee.course_management.exception.BadRequestException;
import com.j2ee.course_management.model.User;
import com.j2ee.course_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(username +" ---------------------------");
		try {
			System.out.println("Username is username");
			System.out.println("Test1");
			System.out.println(passwordEncoder.encode("password"));
			System.out.println("Testing ******");

			User user = this.userRepository.getOne(1L);
			//System.out.println(user);
			//System.out.println(user.getFirstName());
			System.out.println(passwordEncoder.encode("password"));
			if (user != null) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(user.getRole());
				return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
						true, true, true, true, authorities);
			}
			throw new UsernameNotFoundException(String.format("Username[%s] not found", username));
		} catch (RuntimeException ex) {
			throw BadRequestException.create(ex.getMessage());
		}
	}

	private Boolean isEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
				"[a-zA-Z0-9_+&*-]+)*@" +
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
				"A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			throw BadRequestException.create("Username/Email can not be empty");
		return pat.matcher(email).matches();
	}
}
