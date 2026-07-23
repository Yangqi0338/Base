package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author niu
 * @description: 初始化财务请求对象
 * @date 2023/12/18 10:08
 */
@Data
public class InitFinanceReq implements Serializable {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 客户名称
     */
    private String accountName;

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser financeUser;

    /**
     * 无需子账户传null
     * 若需开通子账户必传  集合长度==子账户数量  集合元素代表子账户类型
     * 同一accountId,同一账户类型，子账户类型唯一
     */
    private List<PurseEnum.PurseType> subPurseType;

    /**
     * 上级id 没有上级传0
     */
    private Long parentId;

    /**
     * 杠杆比例
     */
    private Integer leverageRatio;

}
