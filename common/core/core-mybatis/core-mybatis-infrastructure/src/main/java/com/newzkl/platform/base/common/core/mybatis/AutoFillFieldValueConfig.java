package com.newzkl.platform.base.common.core.mybatis;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
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
        Object createTime = this.getFieldValByName("createTime", metaObject);
        if (ObjectUtil.isEmpty(createTime)) {
            this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }
        Object creator = this.getFieldValByName("creator", metaObject);
        // 实体里主动指定了creator
        if (ObjectUtil.isEmpty(creator)) {
            this.setFieldValByName("creator", Opt.ofNullable(SecurityUtils.getNickName()).orElse(""), metaObject);
        }
        Object delFlag = this.getFieldValByName("delFlag", metaObject);
        if (ObjectUtil.isEmpty(delFlag)) {
            this.setFieldValByName("delFlag", 0, metaObject);
        }

        updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        Object updater = this.getFieldValByName("updater", metaObject);
        if (ObjectUtil.isEmpty(updater)) {
            this.setFieldValByName("updater", Opt.ofNullable(SecurityUtils.getNickName()).orElse(""), metaObject);
        }
    }
}
