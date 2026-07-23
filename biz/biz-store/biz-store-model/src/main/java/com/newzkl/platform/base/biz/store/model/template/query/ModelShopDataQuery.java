package com.newzkl.platform.base.biz.store.model.template.query;

import com.newzkl.platform.base.biz.store.model.enums.BIEnum;
import lombok.Data;

@Data
public class ModelShopDataQuery {

    /**
     * 维度
     * DATE(0, "当日"),
     * SEVEN_DATE(1, "近7天"),
     * WEEKLY(2, "本周"),
     * MONTHLY(3, "近30天"),
     * SEASON(4, "本季度"),
     * YEAR(5, "本年")
     */
    private BIEnum.Dimension dimension;

    /**
     * 样板店id
     */
    private Long modelShopId;
}
