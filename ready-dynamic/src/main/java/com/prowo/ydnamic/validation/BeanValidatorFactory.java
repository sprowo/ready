package com.prowo.ydnamic.validation;

import com.prowo.ydnamic.exception.CustomException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

public class BeanValidatorFactory {

    public static <T> void validate(T t) throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> sts = validator.validate(t);
        Iterator<ConstraintViolation<T>> it = sts.iterator();

        if (it.hasNext()) {
            ConstraintViolation<T> con = it.next();
            throw new CustomException(con.getMessage());
        }
    }

}
