package com.berge.ratenow.testapplication.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.berge.ratenow.testapplication.entities.security.Authority;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    List<Authority> findAllByIdIn(List<Long> ids);
    Optional<Authority> findById(Long id);
}
