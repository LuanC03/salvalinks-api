package com.salvalinks.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvalinks.models.User;
import com.salvalinks.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public List<User> getAll() {
		return this.userRepository.findAll();
	}

	public User cadastrar(User user) throws Exception {
		this.userRepository.save(user);
		return user;

	}
}
