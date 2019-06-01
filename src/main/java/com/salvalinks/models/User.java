package com.salvalinks.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.salvalinks.services.NameComparator;

@Document(collection = "users")
public class User {
	@Id
	private String id;
	private String name;
	private String email;
	private String password;
	private Set<Link> links;
	private Set<Notification> notifications;
	private String validationCode;
	private boolean enabled;

	public User() {

	}

	public User(String name, String email, String password, String code) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.links = new HashSet<>();
		this.notifications = new HashSet<>();
		this.enabled = false;
		this.validationCode = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Link> getLinks() {
		return links;
	}

	public void setLinks(Set<Link> links) {
		this.links = links;
	}
	
	public void setEnabled(boolean b) {
		this.enabled = b;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public Set<Notification> getNotifications() {
		return notifications;
	}
	
	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	public boolean containsLink(String url) {
		boolean retorno = false;
		Iterator<Link> iterator = this.getLinks().iterator();
		while (iterator.hasNext()) {
			if (((Link) iterator.next()).getHref().equals(url))
				retorno = true;
		}
		return retorno;
	}

	public Link removeLink(String url) {
		Link retorno = null;
		Iterator<Link> iterator = this.getLinks().iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			if (link.getHref().equals(url))
				retorno = link;
		}
		this.getLinks().remove(retorno);
		return retorno;
	}
	
	public Notification removeNotification(String url) {
		Notification retorno = null;
		Iterator<Notification> iterator = this.getNotifications().iterator();
		while (iterator.hasNext()) {
			Notification notif = (Notification) iterator.next();
			if (notif.getLinkId().equals(url))
				retorno = notif;
		}
		this.getNotifications().remove(retorno);
		return retorno;
	}
	
	public List<Link> orderByNome() {
		List<Link> list = this.toArray();
        Collections.sort(list, new NameComparator());
        return list;
    }
	
	public List<Link> orderByDate() {
		List<Link> list = this.toArray();
        Collections.reverse(list);
        return list;
    }

	public List<Link> toArray() {
		ArrayList<Link> list = new ArrayList<>(this.getLinks());
		return list;
	}
	
	@Override
	public String toString() {
		return "User{" + ", name='" + name + '\'' + ", Email=" + email + '}';
	}


}