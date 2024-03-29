package com.salvalinks.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salvalinks.models.Link;
import com.salvalinks.models.Notification;
import com.salvalinks.models.User;
import com.salvalinks.services.UserService;
@Service
public class NotificationService {

	@Autowired
	UserService userService;
	
	@Autowired
	private Util util;
	
	public Notification createNotification(String email, Link link, String notificationTime) throws Exception {
		User user = this.userService.getByEmail(email);
		String linkId = link.getHref();
		Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(notificationTime);
		String id = util.encrypt(linkId);
		Notification notif = new Notification(id, linkId, date);
		user.getNotifications().add(notif);
		this.userService.saveUser(user);
		return notif;
	}
	
	public Notification deleteNotification(String email, String id) {
		User user = this.userService.getByEmail(email);
		Notification notif = user.removeNotification(id);
		this.userService.saveUser(user);
		return notif;
	}
	
	private Notification getById(String email, String id) {
		User user = this.userService.getByEmail(email);
		Notification retorno = null;
		Iterator<Notification> iterator = user.getNotifications().iterator();
		while (iterator.hasNext()) {
			Notification notification = (Notification) iterator.next();
			if (notification.getId().equals(id))
				retorno = notification;
		}
		return retorno;
	}

	public Notification viewNotification(String email, String id) {
		User user = this.userService.getByEmail(email);
		Notification notif = this.getById(email, id);
		Notification retorno = notif;
		notif.setVisualized(true);
		user.getNotifications().add(notif);
		this.userService.saveUser(user);
		return retorno;
	}
	
	public Notification setNewNotificationTime(String email, String id, Date notificationTime) {
		User user = this.userService.getByEmail(email);
		Notification notif = this.getById(email, id);
		notif.setTime(notificationTime);
		user.getNotifications().add(notif);
		this.userService.saveUser(user);
		return notif;
	}

	
}
