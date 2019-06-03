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

import com.salvalinks.models.Link;
import com.salvalinks.services.LinkService;
import com.salvalinks.services.UserService;

@RestController
@CrossOrigin(value = "*")
public class LinkController {

	@Autowired
	LinkService linkService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public Set<Link> getLinks(HttpServletRequest http) throws Exception {
		return this.linkService.getLinks(this.userService.checkJWT(http.getHeader("Authorization")));
	}

	@RequestMapping(value = "/links/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Link> addLink(@RequestBody Link link, HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(this.linkService.addLink(this.userService.checkJWT(http.getHeader("Authorization")),
				link.getName(), link.getHref(), link.getImportance()), HttpStatus.OK);
	}

	@RequestMapping(value = "/links/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ResponseEntity<Link> removeLink(@RequestParam String url, HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(
				this.linkService.removeLink(this.userService.checkJWT(http.getHeader("Authorization")), url),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/links/rename", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<Link> renameLink(@RequestParam String url, @RequestParam String newName,
			HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(
				this.linkService.renameLink(this.userService.checkJWT(http.getHeader("Authorization")), url, newName),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/links/listbyname", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<List<Link>> listByName(HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(
				this.linkService.listByName(this.userService.checkJWT(http.getHeader("Authorization"))), HttpStatus.OK);
	}

	@RequestMapping(value = "/links/listbydate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<List<Link>> listByDate(HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(
				this.linkService.listByDate(this.userService.checkJWT(http.getHeader("Authorization"))), HttpStatus.OK);
	}

}
