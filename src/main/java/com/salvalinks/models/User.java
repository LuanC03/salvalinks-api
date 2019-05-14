package com.salvalinks.models;

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
	
	@Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", Email=" + email +
                '}';
    }

	
}