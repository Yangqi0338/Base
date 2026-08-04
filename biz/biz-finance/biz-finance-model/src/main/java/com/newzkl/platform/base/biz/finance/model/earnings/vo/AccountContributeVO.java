package com.newzkl.platform.base.biz.finance.model.earnings.vo;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 客户贡献数据
 * @date 2024/1/25 10:55
 */
@Data
public class AccountContributeVO {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 客户姓名
     */
    private String accountName;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 总消费
     */
    private Integer totalConsume;

    /**
     * 分润贡献
     */
    private Integer earningContribute;

    /**
     * 服务费贡献
     */
    private Integer serviceChangeContribute;
}
