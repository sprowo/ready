package com.prowo.ydnamic.annotation;

import com.prowo.ydnamic.validation.YDMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = YDMobileValidator.class)
public @interface YDMobile {

    String message() default "{validator.phone}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
