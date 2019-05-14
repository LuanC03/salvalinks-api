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

	/**
	 * Retorna todos os usuários cadastrados no sistema
	 * 
	 */
	@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public List<User> getUsers() {
		return userService.getAll();
	}
	
	/**
	 * Cadastra um usuário no sitema
	 * @param user
	 * @throws Exception
	 */
	@RequestMapping(value = "/users/cadastrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<User> addClient(@RequestBody User user) throws Exception {
		return new ResponseEntity<>(this.userService.registerUser(user),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/logar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) throws Exception {
		return new ResponseEntity<>(userService.logar(email, password), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public Set<Link> getLinks(@RequestParam String email) throws Exception {
		return userService.getLinks(email);
	}
	
	@RequestMapping(value = "/users/delete", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> deleteAll() throws Exception {
		this.userService.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "links/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Link> addLink(@RequestParam String email, @RequestBody Link link) throws Exception {
		return new ResponseEntity<>(userService.addLink(email, link.getName(),link.getHref(),link.getImportance(),link.getType()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "links/remove", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> removeLink(@RequestParam String email, @RequestParam String href) throws Exception {
		this.userService.removeLink(email,href);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}