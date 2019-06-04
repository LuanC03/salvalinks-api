package com.salvalinks.models;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class Group {
	
	@Id
	private String id;
	private String name;
	private Set<String> links;
	
	public Group(String id, String name) {
		this.id = id;
		this.name = name;
		this.links = new HashSet<>();
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<String> getLinks() {
		return links;
	}
	
	
}
