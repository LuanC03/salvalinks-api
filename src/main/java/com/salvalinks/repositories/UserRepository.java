package com.salvalinks.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.salvalinks.models.User;
 
public interface UserRepository extends MongoRepository<User, Integer> {
    User findByName(String name);
    User findByEmail(String email);
}

