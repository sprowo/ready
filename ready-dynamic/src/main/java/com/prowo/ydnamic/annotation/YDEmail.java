package com.prowo.ydnamic.annotation;

import com.prowo.ydnamic.validation.YDEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = YDEmailValidator.class)
@Documented
public @interface YDEmail {

    String message() default "{validator.email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
