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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			if(isEmail(username)){
				User user = this.userRepository.findByEmail(username);
				if (user != null) {
					List<GrantedAuthority> authorities = new ArrayList<>();
					authorities.add(user.getRole());
					return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
							true, true, true, true, authorities);
				}
				throw new UsernameNotFoundException(String.format("Email[%s] not found", username));
			}

			User user = this.userRepository.findByUsername(username);
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
