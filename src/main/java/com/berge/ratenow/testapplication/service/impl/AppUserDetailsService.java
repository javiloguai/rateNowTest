package com.berge.ratenow.testapplication.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.berge.ratenow.testapplication.entities.security.Authority;
import com.berge.ratenow.testapplication.entities.security.User;
import com.berge.ratenow.testapplication.repositories.security.AuthorityRepository;
import com.berge.ratenow.testapplication.repositories.security.UserRepository;
import com.berge.ratenow.testapplication.utils.seguridad.LoginEntity;
import lombok.Data;

@Data
@Component
@Transactional
public class AppUserDetailsService implements UserDetailsService {
	
	private static final Logger LOGGER = LogManager.getLogger(AppUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public LoginEntity loadUserByUsername(String s) throws UsernameNotFoundException {
        User user  = userRepository.findByUsername(s);

        if (user == null) {
        	LOGGER.debug(String.format("The username %s doesn't exist", s));
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        if (user.isAdmin()) {
            List<Authority> allAuthorities = 	authorityRepository.findAll();
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (Authority authority : allAuthorities) {
                authorities.add(new SimpleGrantedAuthority(authority.getName()));
            }
            LoginEntity a = new LoginEntity(user.getUsername(), user.getPassword(), user.isAdmin(), authorities);
            a.setEnabled(user.isEnabled());
            a.setName(user.getName());
            a.setLastName(user.getLastName());
            return a;
        }

        LoginEntity a = new LoginEntity(user.getUsername(), user.getPassword(), user.isAdmin(), user.getAuthorities());
        a.setEnabled(user.isEnabled());
        a.setName(user.getName());
        a.setLastName(user.getLastName());
        return a;
    }
}
