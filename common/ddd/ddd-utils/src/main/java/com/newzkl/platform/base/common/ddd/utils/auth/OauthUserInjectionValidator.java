package com.newzkl.platform.base.common.ddd.utils.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.newzkl.platform.base.common.ddd.model.auth.OauthRole;
import com.newzkl.platform.base.common.ddd.model.auth.OauthUserId;
import com.newzkl.platform.base.common.ddd.model.auth.OauthUserInjection;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录用户注入校验器
 *
 * <p>类级校验触发时扫描宿主对象带 {@link OauthUserId}/{@link OauthRole} 的字段, 取当前登录
 * 用户ID/角色回填。{@code @OauthUserId} 字段支持 {@code Long}/{@code String};
 * {@code @OauthRole} 字段支持 {@code Long}/{@code String}(角色ID)与 {@code RoleEnum.CompanyRole}(枚举)。
 * 必填却未登录时判校验失败, 否则跳过。</p>
 *
 * <p>字段扫描结果按类缓存, 避免每次请求重复反射。</p>
 *
 * @author KC
 */
public class OauthUserInjectionValidator implements ConstraintValidator<OauthUserInjection, Object> {

    @Override
    public boolean isValid(Object bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return true;
        }
        Field[] fields = resolveFields(bean.getClass());
        if (ArrayUtil.isEmpty(fields)) {
            return true;
        }
        Long accountId = SecurityUtils.getAccountId();
        Long roleId = SecurityUtils.getRoleId();
        boolean valid = true;
        for (Field field : fields) {
            OauthUserId userMark = field.getAnnotation(OauthUserId.class);
            OauthRole roleMark = field.getAnnotation(OauthRole.class);
            if (userMark != null) {
                if (accountId == null) {
                    if (userMark.required()) {
                        valid = false;
                    }
                } else {
                    injectUser(bean, field, accountId);
                }
            }
            if (roleMark != null) {
                if (roleId == null) {
                    if (roleMark.required()) {
                        valid = false;
                    }
                } else {
                    injectRole(bean, field, roleId);
                }
            }
        }
        return valid;
    }

    /**
     * 将账号ID按字段类型回填
     *
     * @param bean      宿主对象
     * @param field     目标字段
     * @param accountId 账号ID
     */
    private void injectUser(Object bean, Field field, Long accountId) {
        try {
            Class<?> type = field.getType();
            if (Long.class.equals(type)) {
                field.set(bean, accountId);
            } else if (String.class.equals(type)) {
                field.set(bean, String.valueOf(accountId));
            }
        } catch (IllegalAccessException ignored) {
            // 注入失败不阻断校验主流程
        }
    }

    /**
     * 将角色ID按字段类型回填(支持角色ID或角色枚举)
     *
     * @param bean   宿主对象
     * @param field  目标字段
     * @param roleId 角色ID
     */
    private void injectRole(Object bean, Field field, Long roleId) {
        try {
            Class<?> type = field.getType();
            if (Long.class.equals(type)) {
                field.set(bean, roleId);
            } else if (RoleEnum.CompanyRole.class.equals(type)) {
                field.set(bean, RoleEnum.CompanyRole.getByCode(roleId));
            }
        } catch (IllegalAccessException ignored) {
            // 注入失败不阻断校验主流程
        }
    }

    /**
     * 解析类(含父类链)带 {@link OauthUserId}/{@link OauthRole} 的字段, 结果缓存
     *
     * @param clazz 宿主类
     * @return 标注字段列表, 无则空列表
     */
    private Field[] resolveFields(Class<?> clazz) {
        return ReflectUtil.getFields(clazz, this::isMarked);
    }

    /**
     * 字段是否带任一注入标记
     *
     * @param field 字段
     * @return 是否带 {@link OauthUserId} 或 {@link OauthRole}
     */
    private boolean isMarked(Field field) {
        for (Class<? extends Annotation> mark : List.of(OauthUserId.class, OauthRole.class)) {
            if (field.isAnnotationPresent(mark)) {
                return true;
            }
        }
        return false;
    }
}
