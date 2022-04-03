package com.berge.ratenow.testapplication.repositories.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.berge.ratenow.testapplication.entities.security.User;

/**
 * @author jruizh
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    
    Optional<User> findById(Long id);

    User findByUsernameAndIdNot(String username, Long id);

	boolean existsByUsername(String username);

}
