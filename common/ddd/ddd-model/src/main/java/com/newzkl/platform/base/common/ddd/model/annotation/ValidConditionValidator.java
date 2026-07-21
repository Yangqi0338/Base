package com.newzkl.platform.base.common.ddd.model.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class ValidConditionValidator implements ConstraintValidator<ValidCondition, Object> {

    private String conditionExpression;
    private ExpressionParser parser;

    @Override
    public void initialize(ValidCondition constraintAnnotation) {
        this.conditionExpression = constraintAnnotation.value();
        this.parser = new SpelExpressionParser();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
//        // 获取当前验证的对象
//        Object validatedObject = context.getRootBean();
//
//        // 创建表达式上下文
//        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(validatedObject);
//
//        try {
//            // 解析并计算表达式
//            Boolean conditionMet = parser.parseExpression(conditionExpression).getValue(evaluationContext, Boolean.class);
//
//            // 如果条件不成立，禁用当前字段的所有其他验证
//            if (conditionMet == null || !conditionMet) {
//                context.disableDefaultConstraintViolation();
//                // 返回true表示本验证器通过，同时其他验证已被禁用
//                return true;
//            }
//
//            // 条件成立，返回true表示继续执行其他验证
//            return true;
//        } catch (Exception e) {
        // 表达式解析失败时视为验证失败
        return false;
//        }
    }
}
