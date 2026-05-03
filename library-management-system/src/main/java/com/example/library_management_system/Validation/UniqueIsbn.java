package com.example.library_management_system.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueIsbnValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueIsbn {
    String message() default "this ISBN already used";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
