package com.salvalinks.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "links")
public class Link {
	
	@Id
	private String id;
	private String name;
	private String data;
	private String group;
	
	public Link() {
		
	}
	
	public Link(String name) {
		this.name = name;
		this.data = new Date().toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getData() {
		return data;
	}
	
	public String getGroup() {
		return group;
	}
	
	@Override
	public String toString() {
		return "Nome: " + name + "\n" + "Data: " + data;
	}
	
}
