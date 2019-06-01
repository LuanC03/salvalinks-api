package com.salvalinks.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


public class Notification {

	@Id
	private String id;
	private String linkId;
	private boolean visualized = false;
	@DateTimeFormat(iso = ISO.TIME)
	private Date notificationTime;

	
	public Notification() {
		
	}
	
	public Notification(String linkId, Date notificationTime, String id) {
		this.id = id;
		this.linkId = linkId;
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

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public boolean isVisualized() {
		return visualized;
	}

	public void setVisualized(boolean visualized) {
		this.visualized = visualized;
	}
}
