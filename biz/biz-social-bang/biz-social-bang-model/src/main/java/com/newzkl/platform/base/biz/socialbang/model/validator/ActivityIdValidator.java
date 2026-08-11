package com.newzkl.platform.base.biz.socialbang.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 活动ID格式校验逻辑
 *
 * <p>正则 {@code ^FHC\d{14}\d{6}$}, 示例 FHC20250831103030000001。空值放行, 交由 @NotNull 处理</p>
 *
 * @author niu
 */
public class ActivityIdValidator implements ConstraintValidator<ActivityIdValid, String> {

    private static final String PATTERN = "^FHC\\d{14}\\d{6}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return value.matches(PATTERN);
    }
}
