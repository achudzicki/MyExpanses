package com.chudzick.expanses.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.TYPE,ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomPasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "Hasło musi posiadać przynajmniej jeden znak szczególny i liczbę";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
