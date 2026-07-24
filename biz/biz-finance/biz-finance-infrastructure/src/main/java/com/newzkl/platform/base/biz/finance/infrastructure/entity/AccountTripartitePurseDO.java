package com.newzkl.platform.base.biz.finance.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * @author 三方账户信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class AccountTripartitePurseDO extends BaseDO {
    /**
     * 客户id
     */
    @Index
    private Long accountId;

    /**
     * 三方用户id
     */
    private String oidUserNo;

    /**
     * 三方申请id
     */
    private String oidApplySeqNo;

    /**
     * 状态
     */
    private String userStatus;

    /**
     * 账户姓名
     */
    private String accountName;

    /**
     * 三方账户余额
     */
    private Integer amount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型
     */
    private PurseEnum.TripartitePurchasePlatform accountType;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 资料信息
     */
    private String commitInfo;
}