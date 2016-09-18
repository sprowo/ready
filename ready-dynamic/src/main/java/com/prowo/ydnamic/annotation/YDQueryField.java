package com.prowo.ydnamic.annotation;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 查询字段注解，主要用于查询字段与数据库字段的映射
 */
@Documented
@Target({FIELD})
@Retention(RUNTIME)
public @interface YDQueryField {

    String field() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
