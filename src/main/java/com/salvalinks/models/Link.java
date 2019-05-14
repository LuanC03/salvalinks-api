package com.salvalinks.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "links")
public class Link {
	@Id
	private String id;
	private String name;
	private String href;
	private String data;
	private String group;
	private String importance;
	private String type;
	
	public Link() {
		
	}
	
	public Link(String name, String href, String importance, String type) {
		this.name = name;
		this.href = href;
		this.data = new Date().toString();
		this.importance = "NORMAL";
		this.type = type;
	}

	public String getName() {
		return this.name;
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
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Nome: " + name + "\n" + "Data: " + data;
	}
	
}
