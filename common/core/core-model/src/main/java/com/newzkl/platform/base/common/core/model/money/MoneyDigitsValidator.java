package com.newzkl.platform.base.common.core.model.money;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@link MoneyDigits} 校验器，依据分值范围与正数约束做判断
 */
public class MoneyDigitsValidator implements ConstraintValidator<MoneyDigits, Money> {

    private long min;
    private long max;
    private boolean positive;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(MoneyDigits constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.positive = constraintAnnotation.positive() == CommonEnum.YesOrNo.YES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Money money, ConstraintValidatorContext context) {
        if (money == null) {
            return true;
        }
        long cent = money.getCent();
        if (cent < min || cent > max) {
            return false;
        }
        return !positive || cent > 0;
    }
}
