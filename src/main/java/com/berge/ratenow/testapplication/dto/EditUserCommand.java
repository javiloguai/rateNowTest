package com.berge.ratenow.testapplication.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author jruizh
 *
 */
@Data
public class EditUserCommand {

	private Long id;
	@NotNull
	private String username;
	@NotNull
	private String name;
	@NotNull
	private String lastName;
	private Boolean admmin;

	private Boolean enabled;

	private List<Long> rolesAssigned;
}
