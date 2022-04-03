package com.berge.ratenow.testapplication.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

import com.berge.ratenow.testapplication.utils.validators.UniqueUser;

import java.util.List;

@Data
@UniqueUser
public class UserCommand {

	private Long id;
	@NotNull
	private String username;
	@NotNull
	private String password;
	@NotNull
	private String name;
	@NotNull
	private String lastName;
	private Boolean admmin;

	private Boolean enabled;

	private List<Long> rolesAssigned;
}
