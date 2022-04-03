package com.berge.ratenow.testapplication.utils.validators;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.berge.ratenow.testapplication.dto.ChangePasswordCommand;
import com.berge.ratenow.testapplication.entities.security.User;
import com.berge.ratenow.testapplication.repositories.security.UserRepository;
import com.berge.ratenow.testapplication.utils.seguridad.SeguridadUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ChangePasswordValidator implements ConstraintValidator<ChangePassword, ChangePasswordCommand> {

    @Autowired
    private UserRepository usuarioRepository;
    @Autowired

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void initialize(ChangePassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(ChangePasswordCommand value, ConstraintValidatorContext context) {
        User usuario = usuarioRepository.findByUsername(SeguridadUtil.autheticatedUserName());

        if (value.getCurrentPassword() == null) {
            return true;
        }

        if (value.getCurrentPassword().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("usuario.changePassword.currentPassword.empty")
                    .addPropertyNode("currentPassword")
                    .addConstraintViolation();
            return false;
        }

        if (!bCryptPasswordEncoder.matches(value.getCurrentPassword(), usuario.getPassword())) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("usuario.changePassword.currentPassword")
                    .addPropertyNode("currentPassword")
                    .addConstraintViolation();
            return false;
        }
        if (!value.getNewPassword().equals(value.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("usuario.changePassword.confirmPassword")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }

        if (value.getNewPassword().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("usuario.changePassword.newPassword")
                    .addPropertyNode("newPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
