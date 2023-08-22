package ru.ermolaayyyyyyy.leschats.presentation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {
    @Override
    public void initialize(ValidRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if(name == null) {
            return false;
        }
        return Objects.equals(name, "ROLE_ADMIN") || name.equals("ROLE_USER");
    }
}
