package com.salvalinks.services;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvalinks.models.Link;
import com.salvalinks.models.Notification;
import com.salvalinks.models.User;
import com.salvalinks.repositories.UserRepository;

@Service
public class NotificationService {

	@Autowired
	UserRepository userRepository;
	
	public Notification createNotification(String email, Link link, String notificationTime) throws Exception {
		User user = this.userRepository.findByEmail(email);
		String linkId = link.getHref();
		Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(notificationTime);
		Notification notif = new Notification(linkId, date);
		user.getNotifications().add(notif);
		this.userRepository.save(user);
		return notif;
	}
	
	public Notification deleteNotification(String email, Link link) {
		User user = this.userRepository.findByEmail(email);
		String linkId = link.getHref();
		Notification notif = user.removeNotification(linkId);
		userRepository.save(user);
		return notif;
	}

	
}
