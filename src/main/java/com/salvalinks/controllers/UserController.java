package com.salvalinks.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salvalinks.models.Link;
import com.salvalinks.models.User;
import com.salvalinks.services.UserService;

@RestController
@CrossOrigin(value = "*")
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public List<User> getUsers() {
		return userService.getAll();
	}
	
	@RequestMapping(value = "/users/cadastrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<User> addClient(@RequestBody User user) throws Exception {
		return new ResponseEntity<>(this.userService.registerUser(user),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/logar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<User> login(@RequestParam String nome, @RequestParam String senha) throws Exception {
		return new ResponseEntity<>(userService.logar(nome, senha), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public Set<Link> getLinks(@RequestBody User user) throws Exception {
		return userService.getLinks(user);
	}
	
	@RequestMapping(value = "links/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<Link> addLink(@RequestBody User user, @RequestParam String name) throws Exception {
		return new ResponseEntity<>(userService.addLink(user, name), HttpStatus.OK);
	}
}