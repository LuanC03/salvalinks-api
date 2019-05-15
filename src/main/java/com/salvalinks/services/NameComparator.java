package com.salvalinks.services;

import java.util.Comparator;

import com.salvalinks.models.Link;

public class NameComparator implements Comparator<Link> {

	public NameComparator() {
		
	}
	
	@Override
	public int compare(Link l1, Link l2) {
		return l1.getName().compareTo(l2.getName());
	}

}