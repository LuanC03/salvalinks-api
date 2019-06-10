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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notification other = (Notification) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (notificationTime == null) {
			if (other.notificationTime != null)
				return false;
		} else if (!notificationTime.equals(other.notificationTime))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	
}
