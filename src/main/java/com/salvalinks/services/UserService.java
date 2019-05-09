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
	
	@Autowired
	Util util;

	public List<User> getAll() {
		return this.userRepository.findAll();
	}

	public User cadastrar(User user) throws Exception {
		if (!util.validaSenha(user.getSenha())) {
			throw new Exception("Senha muito curta");
		}
		
		String senhaCriptografada = util.criptografar(user.getSenha());
		User newUser = new User(user.getName(), user.getEmail(), senhaCriptografada);
		this.userRepository.save(newUser);
		return newUser;

	}
}
