package com.newzkl.platform.base.common.ddd.utils.auth;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import com.newzkl.platform.base.common.core.utils.generator.BusinessCode;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.ddd.model.dto.ExecutorDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mybatis-plus自动填充字段值配置
 *
 * @author 孔祥基
 * @date 2023/3/31 19:56:38
 */

@Component
@Slf4j
public class AutoFillFieldValueConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object executorObj = this.getFieldValByName("executor", metaObject);
        if (executorObj instanceof ExecutorDTO executor) {
            if (ObjectUtil.isEmpty(executor)) {
                executor = new ExecutorDTO();
            }

            executor.setCreatorName(SecurityUtils.getNickName());
            this.setFieldValByName("executor", JSONUtil.toJsonStr(executor), metaObject);
        }
        Object creator = this.getFieldValByName("creator", metaObject);
        // 实体里主动指定了
        if (ObjectUtil.isEmpty(creator)) {
            this.setFieldValByName("creator", SecurityUtils.getAccountId(), metaObject);
        }
        Object createTime = this.getFieldValByName("createTime", metaObject);
        if (ObjectUtil.isEmpty(createTime)) {
            this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }
        Object delFlag = this.getFieldValByName("delFlag", metaObject);
        if (ObjectUtil.isEmpty(delFlag)) {
            this.setFieldValByName("delFlag", 0, metaObject);
        }
        fillBusinessCode(metaObject);
        updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object executorObj = this.getFieldValByName("executor", metaObject);
        if (executorObj instanceof ExecutorDTO executor) {
            if (ObjectUtil.isEmpty(executor)) {
                executor = new ExecutorDTO();
            }

            executor.setUpdater(SecurityUtils.getAccountId());
            executor.setUpdaterName(SecurityUtils.getNickName());
            this.setFieldValByName("executor", JSONUtil.toJsonStr(executor), metaObject);
        }
        // updateTime 是 BaseDO 顶层列(非 ExecutorDTO 字段), 直接填充 metaObject
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    /**
     * DO 类到 @BusinessCode 字段列表的缓存
     */
    private static final Map<Class<?>, List<Field>> CODE_FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 填充标注 @BusinessCode 的空 String 字段
     *
     * <p>仅 insert 期触发,已有非空值则保留,兼容手动 set</p>
     *
     * @param metaObject 元对象
     */
    private void fillBusinessCode(MetaObject metaObject) {
        Object entity = metaObject.getOriginalObject();
        List<Field> fields = CODE_FIELD_CACHE.computeIfAbsent(entity.getClass(), this::resolveCodeFields);
        for (Field field : fields) {
            Object current = this.getFieldValByName(field.getName(), metaObject);
            if (ObjectUtil.isNotEmpty(current)) {
                continue;
            }
            BusinessCode anno = field.getAnnotation(BusinessCode.class);
            String code = BusinessCodeUtil.generate(anno.value(), anno.length());
            this.setFieldValByName(field.getName(), code, metaObject);
        }
    }

    /**
     * 解析类中标注 @BusinessCode 的 String 字段
     *
     * <p>向上遍历父类,只收 String 类型</p>
     *
     * @param clazz DO 类
     * @return 命中字段列表
     */
    private List<Field> resolveCodeFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(BusinessCode.class) && field.getType() == String.class) {
                    result.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return result;
    }
}
