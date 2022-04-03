package com.berge.ratenow.testapplication.server.resume.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.berge.ratenow.testapplication.entities.security.Authority;
import com.berge.ratenow.testapplication.repositories.security.AuthorityRepository;

@RestController
@RequestMapping("/api/admin/authority")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class AuthorityController extends BaseController {
	
	private static final Logger LOGGER = LogManager.getLogger(AuthorityController.class);

    @Autowired
    private AuthorityRepository authorityRepository;

    @GetMapping
    public List<Authority> authorities() {
        return authorityRepository.findAll();
    }

}
