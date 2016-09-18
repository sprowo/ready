package com.prowo.ydnamic.annotation;

import com.prowo.ydnamic.validation.YDLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {YDLengthValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface YDLength {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "{}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}