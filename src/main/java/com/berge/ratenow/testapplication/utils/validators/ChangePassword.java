package com.berge.ratenow.testapplication.utils.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ChangePasswordValidator.class})
public @interface ChangePassword {

    String message() default "User must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
