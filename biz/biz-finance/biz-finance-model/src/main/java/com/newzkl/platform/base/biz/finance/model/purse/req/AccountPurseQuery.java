package com.newzkl.platform.base.biz.finance.model.purse.req;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author niu
 * @description: 客户账户查询
 * @date 2023/12/18 15:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountPurseQuery extends BusinessPageQuery {

    /**
     * 客户类型
     */
    private PurseEnum.FinanceUser accountType;

    /**
     * 账户类型
     */
    private List<PurseEnum.PurseType> purseTypeList;

    public PurseEnum.PurseType getPurseType() {
        return CollUtil.getFirst(purseTypeList);
    }

    public void setPurseType(PurseEnum.PurseType purseType) {
        this.purseTypeList = doWrapperList(purseTypeList, purseType);
    }
}
