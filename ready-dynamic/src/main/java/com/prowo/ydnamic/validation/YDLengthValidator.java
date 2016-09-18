package com.prowo.ydnamic.validation;

import com.prowo.ydnamic.annotation.YDLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YDLengthValidator implements ConstraintValidator<YDLength, CharSequence> {

    private int min;
    private int max;

    public void initialize(YDLength parameters) {
        min = parameters.min();
        max = parameters.max();
    }

    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        int length = value.length();
        return length >= min && length <= max;
    }

}
