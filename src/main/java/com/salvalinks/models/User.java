package com.salvalinks.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.salvalinks.services.DateComparator;
import com.salvalinks.services.NameComparator;

@Document(collection = "users")
public class User {
	@Id
	private String id;
	private String name;
	private String email;
	private String password;
	private Set<Link> links;
	private boolean enabled;

	public User() {

	}

	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.links = new HashSet<>();
		this.enabled = false;
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

	public boolean containsLink(String name) {
		boolean retorno = false;
		Iterator<Link> iterator = this.getLinks().iterator();
		while (iterator.hasNext()) {
			if (((Link) iterator.next()).getName().equals(name)) {
				retorno = true;
			}
		}
		return retorno;
	}

	public Link removeLink(String name) {
		Link retorno = null;
		Iterator<Link> iterator = this.getLinks().iterator();
		while (iterator.hasNext()) {
			Link link = (Link) iterator.next();
			if (link.getName().equals(name)) {
				retorno = link;
			}
		}
		this.getLinks().remove(retorno);
		return retorno;
	}
	
	public List<Link> orderByNome() {
		List<Link> list = this.toArray();
        Collections.sort(list, new NameComparator());
        return list;
    }
	
	public List<Link> orderByDate() {
		List<Link> list = this.toArray();
        Collections.sort(list, new DateComparator());
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

	public void setEnabled(boolean b) {
		this.enabled = b;
		
	}

	public boolean isEnabled() {
		return enabled;
	}

}