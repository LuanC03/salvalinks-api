package com.salvalinks.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvalinks.models.Link;
import com.salvalinks.models.User;
import com.salvalinks.repositories.LinkRepository;
import com.salvalinks.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LinkRepository linkRepository;

	@Autowired
	private Util util;

	public List<User> getAll() {
		return this.userRepository.findAll();
	}
	
	public User getByName(String name) {
		return this.userRepository.findByName(name);
	}

	public User registerUser(User user) throws Exception {
		if (!util.validatePassword(user.getPassword())) {
			throw new Exception("Senha muito curta!");
		}

		String encryptedPassword = util.encrypt(user.getPassword());
		User newUser = new User(user.getName(), user.getEmail(), encryptedPassword);
		this.userRepository.save(newUser);
		return newUser;
	}
	
	public User logar(String name, String password) throws Exception {
		User user = this.getByName(name);
		if (!util.verifyPassword(user.getPassword(), password))
			throw new Exception("Senha incorreta!");
		
		return user;
	}
	
	public Set<Link> getLinks(User user) throws Exception {
		if (user.getLinks().isEmpty()) {
			throw new Exception("Usuario não possui nenhum link!");
		}
		
		return user.getLinks();
	}
	
	public Link getLinkByName(String name) {
		return this.linkRepository.findByName(name);
	}
	
	public Link getLinkById(String id) {
		return this.linkRepository.findById(id);
	}
	
	public Link addLink(User user, String name) throws Exception {
		if (user.getLinks().contains(getLinkByName(name))) {
			throw new Exception("Link já adicionado!");
		}
		Link link = new Link(name);
		user.getLinks().add(link);
		this.userRepository.save(user);
		return link;
	}
	
	public Link removeLink(User user, String name) throws Exception {
		if (!user.getLinks().contains(getLinkByName(name))) {
			throw new Exception("Link não encontrado!");
		}
		Link link = getLinkByName(name);
		user.getLinks().remove(link);
		this.userRepository.save(user);
		return link;
	}
}
