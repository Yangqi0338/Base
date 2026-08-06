package com.newzkl.platform.base.common.ddd.utils.auth;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import com.newzkl.platform.base.common.ddd.model.dto.ExecutorDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
            this.setFieldValByName("creator", SecurityUtils.getDefaultAccountId(), metaObject);
        }
        Object createTime = this.getFieldValByName("createTime", metaObject);
        if (ObjectUtil.isEmpty(createTime)) {
            this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }
        Object delFlag = this.getFieldValByName("delFlag", metaObject);
        if (ObjectUtil.isEmpty(delFlag)) {
            this.setFieldValByName("delFlag", 0, metaObject);
        }
        updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object executorObj = this.getFieldValByName("executor", metaObject);
        if (executorObj instanceof ExecutorDTO executor) {
            if (ObjectUtil.isEmpty(executor)) {
                executor = new ExecutorDTO();
            }

            executor.setUpdater(SecurityUtils.getDefaultAccountId());
            executor.setUpdaterName(SecurityUtils.getNickName());
            this.setFieldValByName("executor", JSONUtil.toJsonStr(executor), metaObject);
        }
        // updateTime 是 BaseDO 顶层列(非 ExecutorDTO 字段), 直接填充 metaObject
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
