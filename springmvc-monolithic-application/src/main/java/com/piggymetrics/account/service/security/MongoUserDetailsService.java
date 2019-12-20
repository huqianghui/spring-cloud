package com.piggymetrics.account.service.security;

import com.piggymetrics.account.domain.User;
import com.piggymetrics.account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MongoUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//TODO
		User temp = new User();
		temp.setUsername(username);
		temp.setPassword(new BCryptPasswordEncoder().encode("12345678"));
		return temp;
		// return repository.findById(username).orElseThrow(()->new UsernameNotFoundException(username));
	}
}
