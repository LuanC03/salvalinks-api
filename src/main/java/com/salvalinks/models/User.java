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
	private Set<Group> groups;
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
		this.groups = new HashSet<>();
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

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	
	public Link getLinkById(String id) {
		Link retorno = null;
		Set<Link> set = this.getLinks();
		Iterator<Link> iterator = set.iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			if (link.getId().equals(id))
				retorno = link;
		}
		
		return retorno;
	}

	public Group containsGroup(String name) {
		Group retorno = null;
		Iterator<Group> iterator = this.getGroups().iterator();
		while (iterator.hasNext()) {
			Group group = (Group) iterator.next();
			if (group.getName().equals(name))
				retorno = group;
		}
		return retorno;
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

	public Link removeLink(String id) {
		Link retorno = null;
		Iterator<Link> iterator = this.getLinks().iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			if (link.getId().equals(id))
				retorno = link;
		}

		if (!retorno.getGroup().equals("none"))
			this.removeFromGroup(retorno.getGroup(), id);

		this.getLinks().remove(retorno);
		return retorno;
	}

	public void removeFromGroup(String nameGroup, String id) {
		Iterator<Group> iterator = this.getGroups().iterator();
		Group retorno = null;
		while (iterator.hasNext()) {
			Group group = (Group) iterator.next();
			if (group.getName().equals(nameGroup))
				retorno = group;
		}
		
		Link link = this.getLinkById(id);
		link.setGroup("none");
		this.removeLink(id);
		this.getLinks().add(link);
		retorno.getLinks().remove(id);

	}
	

	public Notification removeNotification(String id) {
		Notification retorno = null;
		Iterator<Notification> iterator = this.getNotifications().iterator();
		while (iterator.hasNext()) {
			Notification notif = (Notification) iterator.next();
			if (notif.getId().equals(id))
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