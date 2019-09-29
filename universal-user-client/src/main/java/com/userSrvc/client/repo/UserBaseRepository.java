package com.userSrvc.client.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.userSrvc.client.entities.UUserAbs;

@NoRepositoryBean
public interface UserBaseRepository<U extends UUserAbs> extends JpaRepository<U, Long> {
    U getByEmail(String email);
    U getByEmailAndPassword(String email, String password);
    List<U> findByEmailIn(Collection<String> emails);
}
