package com.salvalinks.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.salvalinks.models.Link;
import com.salvalinks.models.User;
import com.salvalinks.repositories.UserRepository;
import com.salvalinks.services.EmailSenderService;
import com.salvalinks.services.UserService;

@RestController
@CrossOrigin(value = "*")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	/**
	 * Retorna todos os usuários cadastrados no sistema
	 * @throws IOException 
	 * 
	 */
	@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public List<User> getUsers() throws IOException {
		return userService.getAll();
	}

	/**
	 * Cadastra um usuário no sistema
	 * 
	 * @param user
	 * @throws Exception
	 */

	@RequestMapping(value = "/users/cadastrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
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

	@RequestMapping(value = "/users/cadastrar/validar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<Boolean> validar(@RequestParam String email, @RequestParam String code) throws Exception {

		return new ResponseEntity<Boolean>(this.userService.validation(email, code), HttpStatus.OK);
	}

	@RequestMapping(value = "/users/logar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) throws Exception {
		return new ResponseEntity<>(userService.logar(email, password), HttpStatus.OK);
	}

	@RequestMapping(value = "/users/delete", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> deleteAll() throws Exception {
		this.userService.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public Set<Link> getLinks(@RequestParam String email) throws Exception {
		return userService.getLinks(email);
	}

	@RequestMapping(value = "links/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Link> addLink(@RequestParam String email, @RequestBody Link link) throws Exception {
		return new ResponseEntity<>(
				userService.addLink(email, link.getName(), link.getHref(), link.getImportance()),
				HttpStatus.OK);
	}

	@RequestMapping(value = "links/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ResponseEntity<Link> removeLink(@RequestParam String email, @RequestParam String name) throws Exception {
		return new ResponseEntity<>(this.userService.removeLink(email, name), HttpStatus.OK);
	}

	@RequestMapping(value = "links/rename", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<Link> renameLink(@RequestParam String email, @RequestParam String name,
			@RequestParam String newName) throws Exception {
		return new ResponseEntity<>(this.userService.renameLink(email, name, newName), HttpStatus.OK);
	}

	@RequestMapping(value = "links/listbyname", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<List<Link>> listByName(@RequestParam String email) throws Exception {
		return new ResponseEntity<>(this.userService.listByName(email), HttpStatus.OK);
	}

	@RequestMapping(value = "links/listbydate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<List<Link>> listByDate(@RequestParam String email) throws Exception {
		return new ResponseEntity<>(this.userService.listByDate(email), HttpStatus.OK);
	}

}