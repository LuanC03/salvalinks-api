package com.salvalinks.controllers;

import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;

import com.salvalinks.models.User;
import com.salvalinks.repositories.UserRepository;
import com.salvalinks.services.UserService;

@RestController
@CrossOrigin(value = "*")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	//////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value = "/redefine", method = RequestMethod.POST)
	public void redefine(@RequestParam("email") String email) throws Exception {
		this.userService.sendLinkToRedefine(email);
	}

	@RequestMapping(value = "/redefinePassword", method = RequestMethod.GET)
	public ModelAndView redefinePassword(ModelAndView modelAndView, @RequestParam("token") String token,
			@RequestParam("email") String email) throws Exception {
		this.userService.validateTokenRedefine(email, token);
		modelAndView.setViewName("redefinePassword");
		return modelAndView;
	}

	@RequestMapping(value = "/redefine", method = RequestMethod.PUT)
	public void redefine(@RequestParam("email") String email, @RequestParam("token") String token,
			@RequestParam("senha") String senha, @RequestParam("senha2") String senha2) throws Exception {
		this.userService.redefinePassword(token, email, senha, senha2);
	}

	//////////////////////////////////////////////////////////////////////////////

	/**
	 * Retorna todos os usuários cadastrados no sistema
	 * 
	 * @throws Exception
	 * 
	 */
	
	@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public List<User> getUsers() throws Exception {
		return userService.getAll();
	}

	/**
	 * Cadastra um usuário no sistema
	 * 
	 * @param user
	 * @throws Exception
	 */

	@RequestMapping(value = "/users/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<User> cadastrar(@RequestBody User user) throws Exception {
		return new ResponseEntity<User>(this.userService.registerUser(user), HttpStatus.OK);
	}

	@RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String token,
			@RequestParam("email") String email) throws Exception {
		this.userService.validation(email, token);
		modelAndView.setViewName("accountVerified");

		return modelAndView;
	}

	@RequestMapping(value = "/users/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) throws Exception {
		return new ResponseEntity<>(userService.logar(email, password), HttpStatus.OK);
	}

	@RequestMapping(value = "/users/delete", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> deleteAll() throws Exception {
		this.userService.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
