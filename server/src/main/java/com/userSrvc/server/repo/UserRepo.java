package com.userSrvc.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userSrvc.server.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User getByEmail(String email);
    User getByEmailAndPassword(String email, String password);
}
