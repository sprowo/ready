package com.prowo.ydnamic.annotation;

import com.prowo.ydnamic.validation.YDNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = YDNotNullValidator.class)
@Documented
public @interface YDNotNull {

    String message() default "{validator.notNull}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
