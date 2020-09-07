package com.chudzick.expanses.validators.register;

import com.chudzick.expanses.domain.users.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        UserDto userDto = (UserDto) o;
        if (userDto.getPassword() == null || userDto.getRepeatedPassword() == null) return true;
        return userDto.getPassword().equals(userDto.getRepeatedPassword());
    }
}
