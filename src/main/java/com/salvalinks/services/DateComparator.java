package com.salvalinks.services;

import java.util.Comparator;

import com.salvalinks.models.Link;

public class DateComparator implements Comparator<Link> {

	public DateComparator() {
		
	}
	
	@Override
	public int compare(Link l1, Link l2) {
		return l2.getData().compareTo(l1.getData());
	}

}