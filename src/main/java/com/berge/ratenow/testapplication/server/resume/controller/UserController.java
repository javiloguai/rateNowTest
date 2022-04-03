package com.berge.ratenow.testapplication.server.resume.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.berge.ratenow.testapplication.dto.EditUserCommand;
import com.berge.ratenow.testapplication.dto.FilterUserCommand;
import com.berge.ratenow.testapplication.dto.UserCommand;
import com.berge.ratenow.testapplication.entities.security.User;
import com.berge.ratenow.testapplication.service.UserService;
import com.berge.ratenow.testapplication.utils.seguridad.SeguridadUtil;

@RestController
@RequestMapping("/api/admin/user")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class UserController {

	private static final Logger LOGGER = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * Creates an user
	 *
	 * @param command UI parameters
	 * @return User
	 */
	@PostMapping
	public User create(@Valid @RequestBody UserCommand command) {

		LOGGER.debug("[User: " + SeguridadUtil.autheticatedUserName() + "] Endpoint /api/admin/user/create >> command: "
				+ command.toString());
		return userService.create(command);
	}

	/**
	 * Uodates an user
	 *
	 * @param command UI parameters
	 * @return an User
	 */
	@PutMapping
	public User update(@Valid @RequestBody EditUserCommand command) {
		LOGGER.debug("[User: " + SeguridadUtil.autheticatedUserName() + "] Endpoint /api/admin/user/update >> command: "
				+ command.toString());
		return userService.update(command);
	}

	/**
	 * Enables/Disables an user
	 *
	 * @param username UI parameter
	 * @return an User
	 */
	@RequestMapping(value = "/{username}", method = RequestMethod.PUT)
	public User enable(@Valid @PathVariable String username) {
		LOGGER.debug("[User: " + SeguridadUtil.autheticatedUserName()
				+ "] Endpoint /api/admin/user/{username} >> username: " + username);
		return userService.changeEnable(username);
	}

	/**
	 * Retusn list of users
	 *
	 * @param command ui filters
	 * @param page    pagination
	 * @param size    pagination
	 * @param sortDir pagination
	 * @param sort    pagination
	 * @return a paginated list
	 */
	@PostMapping("/filter")
	public List<User> filter(@Valid @RequestBody FilterUserCommand command/*
																			 * ,
																			 * 
																			 * @RequestParam(name = "page", required =
																			 * false) Integer page,
																			 * 
																			 * @RequestParam(name = "size", required =
																			 * false) Integer size,
																			 * 
																			 * @RequestParam(name = "sortDir", required
																			 * = false) String sortDir,
																			 * 
																			 * @RequestParam(name = "sort", required =
																			 * false) String sort
																			 */
	) {
		LOGGER.debug("[User: " + SeguridadUtil.autheticatedUserName() + "] Endpoint /api/admin/user/filter >> command: "
				+ command.toString());
		return userService.list(command/* PaginacionUtil.getPageable(page, size, sortDir, sort) */);
	}

}
