package com.salvalinks.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String senha;
    public User() {
    }
 
    public User(String name, String email, String senha) {
        this.name = name;
        this.email = email;
        this.senha = senha;
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
    public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", Email=" + email +
                '}';
    }
}