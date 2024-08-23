package com.springboot.blog.reposistory;

import com.springboot.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    //custom finder method or custom query method  by Spring data jpa
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username,String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
