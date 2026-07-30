package com.newzkl.platform.base.common.core.utils.spring;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SpEL 表达式解析帮助类
 *
 * @author lixianglin
 */
@Slf4j
public class SpElParseUtil {
    private static final SpelExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    public static String generateKeyBySpEL(String spelString, JoinPoint joinPoint) {
        return generateKeyBySpEL(spelString, joinPoint, null);
    }

    public static String generateKeyBySpEL(String spelString, JoinPoint joinPoint, List<String> limitAnnotationList) {
        if (StrUtil.isBlank(spelString)) {
            return spelString;
        }
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String[] paramNames = nameDiscoverer.getParameterNames(method);
            Expression expression = parser.parseExpression(spelString);
            EvaluationContext context = new StandardEvaluationContext();
            List<Object> args = extractAnnoParam(method.getParameters(), joinPoint.getArgs(), limitAnnotationList);
            for (int i = 0; i < args.size(); i++) {
                context.setVariable(paramNames[i], args.get(i));
                context.setVariable("p" + i, args.get(i));
            }
            return expression.getValue(context).toString();
        } catch (Exception e) {
            log.warn("spEl解析失败");
        }
        return "";
    }

    private static List<Object> extractAnnoParam(Parameter[] parameters, Object[] args, List<String> annotationNameList) {
        List<Object> realArgs = new ArrayList<>();
        if (CollUtil.isEmpty(annotationNameList)) {
            return CollUtil.newArrayList(args);
        }
        for (int i = 0; i < parameters.length; i++) {
            Annotation[] annotations = parameters[i].getAnnotations();
            boolean hasAnno = Arrays.stream(annotations).anyMatch(it -> annotationNameList.contains(it.annotationType().getSimpleName()));
            if (hasAnno) {
                realArgs.add(args[i]);
            }
        }
        return realArgs;
    }

    public static String generateKeyBySpEL(String spelString, Map<String, Object> variable) {
        try {
            Expression expression = parser.parseExpression(spelString);
            EvaluationContext context = new StandardEvaluationContext();
            variable.forEach(context::setVariable);
            return expression.getValue(context).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
