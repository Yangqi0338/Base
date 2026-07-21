package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import com.newzkl.platform.base.biz.account.model.enums.identity.OperatorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 市场运营商
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OperatorQuery extends BusinessPageQuery {
    /**
     * 区域 Code
     */
    private Integer proxyAreaCode;
    /**
     * 运营类型 0 机构 1 行业 2 区域
     */
    private OperatorEnum.Type type;
    /**
     * 运营类型关联id列表
     */
    private List<String> typeForeignIdList;

    public void setTypeForeignId(String typeForeignId) {
        this.typeForeignIdList = doWrapperList(this.typeForeignIdList, typeForeignId);
    }
    /**
     * 非运营类型关联id列表
     */
    private List<String> notTypeForeignIdList;
    /**
     * 运营类型关联id
     */
    private String typeForeignNameEq;
    /**
     * 域名地址
     */
    private String domain;

    /**
     * 供应商商品数量
     */
    private Integer supplierGoodsCount;
}
