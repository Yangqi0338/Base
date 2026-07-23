package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model;

import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import lombok.Getter;
import lombok.Setter;
import org.dromara.autotable.annotation.Index;

/**
 * 基础数据实体类
 *
 * @author god
 */
@Setter
@Getter
public class AuditBaseDO extends BaseDO {

    /**
     * 外键id
     */
    @Index
    private Long foreignId;
}
