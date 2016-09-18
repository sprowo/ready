package com.prowo.ydnamic.validation;

import com.prowo.ydnamic.annotation.YDNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YDNotNullValidator implements ConstraintValidator<YDNotNull, Object> {

    @Override
    public void initialize(YDNotNull arg0) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext arg1) {
        if (value == null || "".equals(value.toString())) {
            return false;
        }
        return true;
    }


}
