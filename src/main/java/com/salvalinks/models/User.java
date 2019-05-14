package com.salvalinks.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
	@Id
	private String id;
	private String name;
	private String email;
	private String password;
	private Set<Link> links;

	public User() {

	}

	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.links = new HashSet<>();
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

	public boolean containsLink(String href) {
		boolean retorno = false;
		Iterator iterator = this.getLinks().iterator();
		while (iterator.hasNext()) {
			if (((Link) iterator.next()).getHref().equals(href)) {
				retorno = true;
			}
		}
		return retorno;
	}

	public void removeLink(String href) {
		Iterator iterator = this.getLinks().iterator();
		while (iterator.hasNext()) {
			if (((Link) iterator.next()).getHref().equals(href)) {
				this.getLinks().remove((Link)iterator.next());
			}
		}
	}

	@Override
	public String toString() {
		return "User{" + ", name='" + name + '\'' + ", Email=" + email + '}';
	}

}