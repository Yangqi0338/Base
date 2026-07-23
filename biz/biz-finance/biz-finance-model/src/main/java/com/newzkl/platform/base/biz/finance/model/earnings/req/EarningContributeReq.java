package com.newzkl.platform.base.biz.finance.model.earnings.req;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;

/**
 * @author 个人贡献表
 */
@Data
public class EarningContributeReq extends BaseRes {

    private static final long serialVersionUID = 1L;
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