package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import com.newzkl.platform.base.biz.account.model.enums.AuthEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmpDO extends BaseDO {
    //类型
    private AuthEnum.EmpType type;
    private String jobIdList;
}
