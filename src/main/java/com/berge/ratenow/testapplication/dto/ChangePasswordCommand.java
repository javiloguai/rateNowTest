package com.berge.ratenow.testapplication.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

import com.berge.ratenow.testapplication.utils.validators.ChangePassword;

/**
 * @author jruizh
 *
 */
@Data
@ChangePassword
public class ChangePasswordCommand {

//    @NotNull
	private String currentPassword;
	@NotNull
	private String newPassword;
//    @NotNull
	private String confirmPassword;
}
