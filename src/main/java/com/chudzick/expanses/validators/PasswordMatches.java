package com.chudzick.expanses.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatcherValidator.class)
@Documented
public @interface PasswordMatches {
String message() default "Passwords don't match";
Class<?>[] groups() default {};
Class<? extends Payload>[] payload() default {};
}
