package com.qwikTix.qwiktix.users;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users, Integer> {
    Users findByEmailAndPassword(String email, String password);
}
