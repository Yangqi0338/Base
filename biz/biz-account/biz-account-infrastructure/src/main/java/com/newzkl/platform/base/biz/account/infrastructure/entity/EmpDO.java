package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.biz.account.model.enums.AuthEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class EmpDO extends BaseDO {
    /**
     * 类型
     */
    private AuthEnum.EmpType type;
}
