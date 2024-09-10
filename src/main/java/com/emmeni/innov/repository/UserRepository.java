package com.emmeni.innov.repository;

import java.util.Optional;

import com.emmeni.innov.domain.User;

public interface UserRepository extends PersistentRepository<User> {


    Optional<User> findByEmailOrUsername(String email, String username);

    User findByUsername(String username);
}
