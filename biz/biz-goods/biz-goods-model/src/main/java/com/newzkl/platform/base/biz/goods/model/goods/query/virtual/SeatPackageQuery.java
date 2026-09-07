package com.newzkl.platform.base.biz.goods.model.goods.query.virtual;

import com.newzkl.platform.base.common.ddd.model.auth.OauthIdentity;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;

/**
 * 席位套餐分页查询
 */
@Data
public class SeatPackageQuery extends PageQuery {

    /**
     * 查询内容
     * @ext 匹配套餐名称/ID
     */
    private String searchContent;

    /**
     * 状态
     * @ext 取值范围: 0=禁用, 1=启用
     */
    private Integer state;

    /**
     * 角色
     */
    @OauthIdentity
    private AccountEnum.Identity identity;

}
