package com.berge.ratenow.testapplication.service;


import java.util.List;

import com.berge.ratenow.testapplication.dto.ChangePasswordCommand;
import com.berge.ratenow.testapplication.dto.EditUserCommand;
import com.berge.ratenow.testapplication.dto.FilterUserCommand;
import com.berge.ratenow.testapplication.dto.ResetPasswordCommand;
import com.berge.ratenow.testapplication.dto.UserCommand;
import com.berge.ratenow.testapplication.dto.UserDataCommand;
import com.berge.ratenow.testapplication.entities.security.User;

public interface UserService {

    User create(UserCommand command);

    User update(EditUserCommand command);

    User changeEnable(String username);
    
    User changeEnable(Long id);
    
    Boolean delete(Long id);

    List<User> list(FilterUserCommand command);
    
    User getUserData(Long id);

    User changePassword(ChangePasswordCommand command);
    
    User resetPasswordForUser(ResetPasswordCommand command);

    UserDataCommand userData(UserDataCommand command);
    
	Boolean existsUser(String userName);

	User getUserDataByUsername(String autheticatedUserName);

	
}
