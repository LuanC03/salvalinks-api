package com.salvalinks.controllers;

import java.time.LocalDateTime;

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
import com.salvalinks.models.Notification;
import com.salvalinks.services.NotificationService;
import com.salvalinks.services.UserService;

@RestController
@CrossOrigin(value = "*")
public class NotificationController {

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/notification/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Notification> createNotfication(@RequestBody Link link, @RequestParam String notificationTime, HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(this.notificationService.createNotification(this.userService.checkJWT(http.getHeader("Authorization")), link, notificationTime), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/notification/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ResponseEntity<Notification> createNotfication(@RequestBody Link link, HttpServletRequest http) throws Exception {
		return new ResponseEntity<>(this.notificationService.deleteNotification(this.userService.checkJWT(http.getHeader("Authorization")), link), HttpStatus.OK);
	}
	
}