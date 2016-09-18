package com.prowo.ydnamic.annotation;

import com.prowo.ydnamic.validation.YDPhoneMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = YDPhoneMobileValidator.class)
@Documented
public @interface YDPhoneMobile {

    String message() default "{validator.phonemobile}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
