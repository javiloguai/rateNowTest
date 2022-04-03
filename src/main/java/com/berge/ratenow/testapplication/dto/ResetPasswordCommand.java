package com.berge.ratenow.testapplication.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author jruizh
 *
 */
@Data
public class ResetPasswordCommand {

	@NotNull
	private String username;
	@NotNull
	private String newPassword;

}
