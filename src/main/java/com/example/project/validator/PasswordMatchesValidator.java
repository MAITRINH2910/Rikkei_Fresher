package com.example.project.validator;

import com.example.project.DTO.request.SignUpForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        SignUpForm user = (SignUpForm) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}