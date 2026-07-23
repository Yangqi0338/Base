package com.newzkl.platform.base.biz.account.infrastructure.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 职位
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class AccountJobDO extends BaseDO {

    /**
     * 名称
     */
    @Index
    private String name;

    /**
     * 备注
     */
    private String comment;
}