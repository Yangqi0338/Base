package com.newzkl.platform.base.biz.finance.model.purse.req;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 账户变动记录查询对象
 * @date 2023/12/23 16:53
 */
@Data
public class AccountPurseAlterRecordQuery extends BizPageQuery {

    /**
     * 账户类型
     */
    private PurseEnum.Type purseType;

    /**
     * 变动类型
     */
    private EarningsEnum.PurseAlterTypeEnum earningAlterType;

    /**
     * 客户类型
     * @ext 个人不传, 平台传
     */
    private PurseEnum.User accountType;

    /**
     * 变动业务类型列表
     */
    private List<PurseEnum.AlterType> alterTypeList;

    /**
     * 分组维度
     */
    private Integer groupDimension;

    public void setAlterType(PurseEnum.AlterType alterType) {
        this.alterTypeList = doWrapperList(alterTypeList, alterType);
    }
}
