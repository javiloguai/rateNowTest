package com.berge.ratenow.testapplication.utils.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.berge.ratenow.testapplication.dto.UserCommand;
import com.berge.ratenow.testapplication.entities.security.User;
import com.berge.ratenow.testapplication.repositories.security.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author jruizh
 *
 */
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, UserCommand> {

    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public void initialize(UniqueUser constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserCommand value, ConstraintValidatorContext context) {
        boolean isEdition = value.getId() != null;
        User usuario = null;
        if (isEdition) {
            usuario = usuarioRepository.findByUsername(value.getUsername());
            if (usuario == null) {
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate("usuario.username.notfound")
                        .addPropertyNode("username")
                        .addConstraintViolation();
                return false;
            }
            usuario = usuarioRepository.findByUsernameAndIdNot(value.getUsername(), value.getId());
        } else {
            usuario = usuarioRepository.findByUsername(value.getUsername());
        }

        if (usuario != null) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("usuario.username.unique")
                    .addPropertyNode("username")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
