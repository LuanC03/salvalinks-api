package com.salvalinks.controllers;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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

import com.salvalinks.models.Link;
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

	@RequestMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public Set<Link> getLinks(HttpServletRequest http) throws Exception {
		return userService.getLinks(this.userService.checkJWT(http.getHeader("Authorization")));
	}

	@RequestMapping(value = "links/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Link> addLink(@RequestBody Link link, HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(userService.addLink(this.userService.checkJWT(http.getHeader("Authorization")),
				link.getName(), link.getHref(), link.getImportance()), HttpStatus.OK);
	}

	@RequestMapping(value = "links/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ResponseEntity<Link> removeLink(@RequestParam String url, HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(
				this.userService.removeLink(this.userService.checkJWT(http.getHeader("Authorization")), url),
				HttpStatus.OK);
	}

	@RequestMapping(value = "links/rename", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<Link> renameLink(@RequestParam String url, @RequestParam String newName,
			HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(
				this.userService.renameLink(this.userService.checkJWT(http.getHeader("Authorization")), url, newName),
				HttpStatus.OK);
	}

	@RequestMapping(value = "links/listbyname", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<List<Link>> listByName(HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(
				this.userService.listByName(this.userService.checkJWT(http.getHeader("Authorization"))), HttpStatus.OK);
	}

	@RequestMapping(value = "links/listbydate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<List<Link>> listByDate(HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(
				this.userService.listByDate(this.userService.checkJWT(http.getHeader("Authorization"))), HttpStatus.OK);
	}

}
