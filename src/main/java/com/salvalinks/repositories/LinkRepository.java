package com.salvalinks.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.salvalinks.models.Link;

public interface LinkRepository extends MongoRepository<Link, Integer> {
	Link findById(String id);
	Link findByName(String name);
}
