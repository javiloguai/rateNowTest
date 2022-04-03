package com.berge.ratenow.testapplication.dto;

import lombok.Data;

/**
 * @author jruizh
 *
 */
@Data
public class FilterUserCommand {

	private String username;
	private String name;
	private String lastName;
	private Long role;
	private Boolean enabled;
}
