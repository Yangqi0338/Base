package com.newzkl.platform.base.biz.store.model.template.query;

import com.newzkl.platform.base.common.ddd.model.enums.finance.BIEnum;
import lombok.Data;

/**
 * 样板店数据查询
 */
@Data
public class ModelShopDataQuery {

    /**
     * 查询维度
     * @ext 前端传数字 code, Jackson 经 BIEnum.Dimension @JsonValue 反序列化
     */
    private BIEnum.Dimension dimension;

    /**
     * 样板店id
     */
    private Long modelShopId;
}
