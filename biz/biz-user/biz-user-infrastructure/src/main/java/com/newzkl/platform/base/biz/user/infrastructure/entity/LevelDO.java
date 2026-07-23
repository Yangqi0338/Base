package com.newzkl.platform.base.biz.user.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.user.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
// TODO[pom-gap mybatis-plus-ext]: import org.dromara.mpe.autofill.annotation.JsonSerializable; (parent 已 depMgmt mybatis-plus-ext, infra 需补依赖)

/**
 * 等级持久化对象。
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class LevelDO extends BaseDO {

    /**
     * 身份类型
     */
    @Index
    private RoleEnum.CompanyRole type;
    /**
     * 是否开启
     */
    private CommonEnum.YesOrNo enable;
    /**
     * 等级名称
     */
    private String name;
    /**
     * 等级权限
     */
    // TODO[pom-gap mybatis-plus-ext]: @JsonSerializable (autofill, parent 已 depMgmt mybatis-plus-ext, infra 需补依赖)
    private String permission;
    /**
     * 升级条件
     */
    // TODO[pom-gap mybatis-plus-ext]: @JsonSerializable (autofill, parent 已 depMgmt mybatis-plus-ext, infra 需补依赖)
    private String condition;
    /**
     * 条件判断类型
     */
    private Integer conditionJudgeType;
}
