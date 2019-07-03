package com.chudzick.expanses.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomEmailValidator.class)
@Documented
public @interface ValidEmail {
    String message() default "{form.validation.invalid.email.format}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
 }
