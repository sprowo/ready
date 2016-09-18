package com.prowo.ydnamic.validation;

import com.prowo.ydnamic.annotation.YDPhoneMobile;
import com.prowo.ydnamic.context.ComxContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YDPhoneMobileValidator implements ConstraintValidator<YDPhoneMobile, Object> {

    @Override
    public void initialize(YDPhoneMobile arg0) {

    }

    @Override
    public boolean isValid(Object phone, ConstraintValidatorContext arg1) {
        boolean flag = false;
        try {
            Pattern pattern = Pattern.compile(ComxContext.getContext().get("mobile.pattern") == null ? "^[0-9]{11,14}$"
                    : ComxContext.getContext().get("mobile.pattern"));
            Matcher matcher = pattern.matcher((CharSequence) phone);
            flag = matcher.matches();
            if (!flag) {
                pattern = Pattern.compile(ComxContext.getContext().get("phone.pattern") == null ? "^[0-9-/\\ ]+$"
                        : ComxContext.getContext().get("phone.pattern"));
                matcher = pattern.matcher((CharSequence) phone);
                flag = matcher.matches();
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

}
