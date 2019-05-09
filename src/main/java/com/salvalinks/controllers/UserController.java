package com.salvalinks.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	public User addClient(@RequestBody User user) throws Exception {
		return this.userService.cadastrar(user);
	}
	
}