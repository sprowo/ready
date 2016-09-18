package com.prowo.ydnamic.validation;

import com.prowo.ydnamic.annotation.YDMobile;
import com.prowo.ydnamic.context.ComxContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YDMobileValidator implements ConstraintValidator<YDMobile, Object> {

    @Override
    public void initialize(YDMobile arg0) {

    }

    @Override
    public boolean isValid(Object phone, ConstraintValidatorContext arg1) {
        boolean flag = false;
        try {
            Pattern pattern = Pattern.compile(ComxContext.getContext().get("mobile.pattern") == null ? "^[0-9]{11,14}$"
                    : ComxContext.getContext().get("mobile.pattern"));
            Matcher matcher = pattern.matcher((CharSequence) phone);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

}
