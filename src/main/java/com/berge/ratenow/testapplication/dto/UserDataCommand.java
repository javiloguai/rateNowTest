package com.berge.ratenow.testapplication.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jruizh
 *
 */
@Data
public class UserDataCommand {

	@NotNull
	private String name;
	@NotNull
	private String lastName;
}
