package com.salvalinks.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


public class Notification {

	@Id
	private String id;
	private String url;
	private boolean visualized = false;
	@DateTimeFormat(iso = ISO.TIME)
	private Date notificationTime;

	
	public Notification() {
		
	}
	
	public Notification(String id, String url, Date notificationTime) {
		this.id = id;
		this.url = url;
		this.notificationTime = notificationTime;
	}
	
	public String getId() {
		return id;
	}
	
	public Date getTime() {
		return notificationTime;
	}

	public void setTime(Date time) {
		this.notificationTime = time;
	}

	public String getUrl() {
		return url;
	}

	public void setLinkId(String url) {
		this.url = url;
	}

	public boolean isVisualized() {
		return visualized;
	}

	public void setVisualized(boolean visualized) {
		this.visualized = visualized;
	}
}
