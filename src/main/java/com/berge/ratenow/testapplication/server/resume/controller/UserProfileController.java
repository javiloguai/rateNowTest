package com.berge.ratenow.testapplication.server.resume.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.berge.ratenow.testapplication.dto.ChangePasswordCommand;
import com.berge.ratenow.testapplication.dto.UserDataCommand;
import com.berge.ratenow.testapplication.entities.security.User;
import com.berge.ratenow.testapplication.service.UserService;
import com.berge.ratenow.testapplication.utils.seguridad.SeguridadUtil;
@PreAuthorize("hasAnyAuthority('ADMIN')")
@RestController
public class UserProfileController extends BaseController {

	private static final Logger LOGGER = LogManager.getLogger(UserProfileController.class);

    @Autowired
    private UserService userService;

    @PutMapping("/api/profile/changePassword")
    public User changePassword(
            @Valid @RequestBody ChangePasswordCommand command
    ) {
    	LOGGER.debug("[User: " + SeguridadUtil.autheticatedUserName() + "] Endpoint/api/profile/changePassword >> command: " + command.toString());
        return userService.changePassword(command);
    }

    @PutMapping("/api/profile/userData")
    public UserDataCommand userData(
            @Valid @RequestBody UserDataCommand command
    ) {
    	LOGGER.debug("[User: " + SeguridadUtil.autheticatedUserName() + "] Endpoint /api/profile/userData >> command: " + command.toString());
        return userService.userData(command);
    }
}
