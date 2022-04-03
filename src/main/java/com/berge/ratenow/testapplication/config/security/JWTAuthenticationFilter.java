package com.berge.ratenow.testapplication.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.berge.ratenow.testapplication.constants.SecurityConstants;
import com.berge.ratenow.testapplication.service.impl.AppUserDetailsService;
import com.berge.ratenow.testapplication.utils.seguridad.LoginCommand;
import com.berge.ratenow.testapplication.utils.seguridad.LoginEntity;
import com.berge.ratenow.testapplication.utils.seguridad.SeguridadUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private AppUserDetailsService appUserDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    private static final Logger LOGGER = LogManager.getLogger(JWTAuthenticationFilter.class);

    Gson gson = new GsonBuilder().setExclusionStrategies(new UserExclusions())
            .create();

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AppUserDetailsService appUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.appUserDetailsService = appUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {

        	LOGGER.debug("Login attempt");
            LoginCommand creds = new ObjectMapper().readValue(req.getInputStream(), LoginCommand.class);
            
            String token = SeguridadUtil.generateToken(creds.getUsername());
            creds.setToken(SecurityConstants.TOKEN_PREFIX + token);
            LOGGER.debug("Token generated >> " + SecurityConstants.TOKEN_PREFIX + token);

            LoginEntity ud = appUserDetailsService.loadUserByUsername(creds.getUsername());

            if (!bCryptPasswordEncoder.matches(creds.getPassword(), ud.getPassword())) {
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                LOGGER.debug("Login Attempt Unauthorized");
                return null;
            }
            if (!ud.isEnabled()) {
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                LOGGER.debug("Login Attempt Unauthorized");
                return null;
            }

            creds.setAuthorities(ud.getAuthorities());
            creds.setLastName(ud.getLastName());
            creds.setName(ud.getName());
            creds.setEnabled(ud.isEnabled());
            creds.setAdmin(ud.isAdmin());

            String userString = this.gson.toJson(creds);

            PrintWriter out = res.getWriter();
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            out.print(userString);
            out.flush();

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
        	LOGGER.error("Login error >> " + e.getMessage());
            throw new RuntimeException(e);            
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = SeguridadUtil.generateToken(((User) auth.getPrincipal()).getUsername());
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        LOGGER.debug("Login Successful");
    }

    static class UserExclusions implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return (f.getDeclaringClass() == LoginCommand.class && f.getName().equals("password"));
        }
    }
}
