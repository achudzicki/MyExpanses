package com.chudzick.expanses.validators.register;

import org.passay.*;
import org.springframework.context.annotation.PropertySource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@PropertySource("messages.properties")
public class CustomPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(getClass().getClassLoader().getResource("messages.properties").getFile())) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageResolver messageResolver = new PropertiesMessageResolver(properties);
        PasswordValidator validator = new PasswordValidator(messageResolver, Arrays.asList(
                new LengthRule(8, 25),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new WhitespaceRule()
        ));

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                validator.getMessages(result).get(0)
        ).addConstraintViolation();
        return false;
    }

}
