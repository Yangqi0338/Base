package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.account.OperatorEnum;
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
public class OperatorQuery extends BizPageQuery {
    /**
     * 区域 Code
     */
    private Integer proxyAreaCode;
    /**
     * 运营类型
     * @ext 前端传数字 code, Jackson 经 OperatorEnum.Type 的 @JsonValue 反序列化为枚举
     */
    private OperatorEnum.Type type;
    /**
     * 运营类型关联id列表
     */
    private List<String> typeForeignIdList;

    /**
     * 设置单个运营类型关联id (内部包装为列表)
     *
     * @param typeForeignId 运营类型关联id
     */
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
