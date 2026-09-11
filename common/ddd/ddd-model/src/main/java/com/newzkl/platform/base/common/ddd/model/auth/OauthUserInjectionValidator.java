package com.newzkl.platform.base.common.ddd.model.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录用户注入校验器
 *
 * <p>类级校验触发时扫描宿主对象带 {@link OauthUserId}/{@link OauthIdentity} 的字段, 取当前登录
 * 用户ID/角色回填。{@code @OauthUserId} 字段支持 {@code Long}/{@code String};
 * {@code @OauthRole} 字段支持 {@code Long}/{@code String}(角色ID)与 {@code RoleEnum.CompanyRole}(枚举)。
 * 必填却未登录时判校验失败, 否则跳过。</p>
 *
 * <p>字段扫描结果按类缓存, 避免每次请求重复反射。</p>
 *
 * @author KC
 */
public class OauthUserInjectionValidator implements ConstraintValidator<OauthUserInjection, Object> {

    private static volatile ConcurrentHashMap<Class<?>, List<AnnotatedElement>> CACHE = new ConcurrentHashMap<>();

    @Override
    public boolean isValid(Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        Class<?> clazz = bean.getClass();
        List<AnnotatedElement> annotatedElementList = CACHE.get(clazz);
        if (annotatedElementList == null) {
            annotatedElementList = new ArrayList<>();
            CollUtil.addAll(annotatedElementList,resolveFields(clazz));
            CollUtil.addAll(annotatedElementList,resolveMethod(clazz));
        }
        Long accountId = SecurityUtils.getAccountId();
        AccountEnum.Identity identity = SecurityUtils.getIdentity();
        boolean valid = true;
        for (AnnotatedElement element : annotatedElementList) {
            if (element.isAnnotationPresent(OauthUserId.class)) {
                OauthUserId userMark = element.getAnnotation(OauthUserId.class);
                if (accountId == null) {
                    if (userMark.required()) {
                        valid = false;
                    }
                } else {
                    if (element instanceof Field field) {
                        injectUser(bean, field, accountId);
                    }else if (element instanceof Method method) {
                        injectUser(bean, method, accountId);
                    }
                }
            }

            if (element.isAnnotationPresent(OauthIdentity.class)) {
                OauthIdentity roleMark = element.getAnnotation(OauthIdentity.class);
                if (identity == null) {
                    if (roleMark.required()) {
                        valid = false;
                    }
                } else {
                    if (element instanceof Field field) {
                        injectRole(bean, field, identity);
                    }else if (element instanceof Method method) {
                        injectRole(bean, method, identity);
                    }
                }
            }
        }
        CACHE.put(clazz, annotatedElementList);
        return valid;
    }

    private void injectRole(Object bean, Method method, AccountEnum.Identity identity) {
        try {
            Parameter parameter = method.getParameters()[0];
            Class<?> type = parameter.getType();
            if (Long.class.equals(type)) {
                method.invoke(bean, identity.getCode());
            } else if (AccountEnum.Identity.class.equals(type)) {
                method.invoke(bean, identity);
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // 注入失败不阻断校验主流程
        }
    }

    private void injectUser(Object bean, Method method, Long accountId) {
        try {
            Parameter parameter = method.getParameters()[0];
            Class<?> type = parameter.getType();
            if (Long.class.equals(type)) {
                method.invoke(bean, accountId);
            } else if (String.class.equals(type)) {
                method.invoke(bean, String.valueOf(accountId));
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // 注入失败不阻断校验主流程
        }
    }

    /**
     * 将账号ID按字段类型回填
     *
     * @param bean      宿主对象
     * @param field     目标字段
     * @param accountId 账号ID
     */
    private void injectUser(Object bean, Field field, Long accountId) {
        Class<?> type = field.getType();
        if (Long.class.equals(type)) {
            ReflectUtil.setFieldValue(bean, field, accountId);
        } else if (String.class.equals(type)) {
            ReflectUtil.setFieldValue(bean, field, String.valueOf(accountId));
        }
    }

    /**
     * 将角色ID按字段类型回填(支持角色ID或角色枚举)
     *
     * @param bean   宿主对象
     * @param field  目标字段
     */
    private void injectRole(Object bean, Field field, AccountEnum.Identity identity) {
        Class<?> type = field.getType();
        if (Long.class.equals(type)) {
            ReflectUtil.setFieldValue(bean, field, identity.getCode());
        } else if (AccountEnum.Identity.class.equals(type)) {
            ReflectUtil.setFieldValue(bean, field, identity);
        }
    }

    /**
     * 解析类(含父类链)带 {@link OauthUserId}/{@link OauthIdentity} 的字段, 结果缓存
     *
     * @param clazz 宿主类
     * @return 标注字段列表, 无则空列表
     */
    private Field[] resolveFields(Class<?> clazz) {
        return ReflectUtil.getFields(clazz, this::isMarked);
    }

    private Method[] resolveMethod(Class<?> clazz) {
        return ReflectUtil.getMethods(clazz, this::isMarked);
    }

    /**
     * 字段是否带任一注入标记
     *
     * @param field 字段
     * @return 是否带 {@link OauthUserId} 或 {@link OauthIdentity}
     */
    private boolean isMarked(Field field) {
        for (Class<? extends Annotation> mark : List.of(OauthUserId.class, OauthIdentity.class)) {
            if (field.isAnnotationPresent(mark)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMarked(Method method) {
        for (Class<? extends Annotation> mark : List.of(OauthUserId.class, OauthIdentity.class)) {
            if (method.isAnnotationPresent(mark) && method.getParameterCount() == 1) {
                return true;
            }
        }
        return false;
    }
}
