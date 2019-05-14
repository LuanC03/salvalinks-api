package com.salvalinks.services;

import java.util.Iterator;
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

	public User getByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	public void deleteAll() {
		this.userRepository.deleteAll();
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

	public User logar(String email, String password) throws Exception {
		User user = this.getByEmail(email);
		if (!util.verifyPassword(user.getPassword(), password))
			throw new Exception("Senha incorreta!");

		return user;
	}

	public Set<Link> getLinks(String email) throws Exception {
		User user = this.userRepository.findByEmail(email);
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
	

	public Link addLink(String email, String name, String href, String importance, String type) throws Exception {
		User user = this.userRepository.findByEmail(email);
		if (user.containsLink(href)) {
			throw new Exception("Link já adicionado!");
		}
		Link link = new Link(name,href,importance,type);
		user.getLinks().add(link);
		this.userRepository.save(user);
		return link;
	}

	public void removeLink(String email, String href) throws Exception {
		User user = this.userRepository.findByEmail(email);
		if (!user.containsLink(href)) {
			throw new Exception("Link não encontrado!");
		}
		user.removeLink(href);
		this.userRepository.save(user);

	}
}
