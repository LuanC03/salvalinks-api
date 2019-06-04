package com.salvalinks.controllers;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salvalinks.models.Group;
import com.salvalinks.models.Link;
import com.salvalinks.services.GroupService;
import com.salvalinks.services.UserService;

@RestController
@CrossOrigin(value = "*")
public class GroupController {

	@Autowired
	GroupService groupService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/group/create", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public Group addGroup(@RequestParam String name, HttpServletRequest http) throws Exception {
		return this.groupService.addGroup(this.userService.checkJWT(http.getHeader("Authorization")), name);
	}

	@RequestMapping(value = "/group/addlink", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<String> addLinkToGroup(@RequestParam String name, @RequestParam String id, HttpServletRequest http)
			throws Exception {
		return new ResponseEntity<>((this.groupService
				.addLinkToGroup(this.userService.checkJWT(http.getHeader("Authorization")), name, id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/group/removelink", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<String> removeLinkFromGroup(@RequestParam String name, @RequestParam String id, HttpServletRequest http)
			throws Exception {
		return new ResponseEntity<>((this.groupService
				.removeLinkFromGroup(this.userService.checkJWT(http.getHeader("Authorization")), name, id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/group/links", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Set<Link>> removeLinkFromGroup(@RequestParam String name, HttpServletRequest http)
			throws Exception {
		return new ResponseEntity<>((this.groupService
				.getLinksFromGroup(this.userService.checkJWT(http.getHeader("Authorization")), name)), HttpStatus.OK);
	}

}
