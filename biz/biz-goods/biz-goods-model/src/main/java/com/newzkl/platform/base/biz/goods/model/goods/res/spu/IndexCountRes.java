package com.newzkl.platform.base.biz.goods.model.goods.res.spu;

import com.newzkl.platform.base.common.ddd.model.GroupCountRes;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/29:38
 */
@Data
public class IndexCountRes {
    /**
     * 分组数据
     */
    private List<GroupCountRes> groupCountRes;
    /**
     * 通过商品数量
     */
    private Integer passGoodsNum;
    /**
     * 审核中商品数量
     */
    private Integer auditGoodsNum;
    /**
     * 未通过商品数量
     */
    private Integer refuseGoodsNum;
}
