package com.newzkl.platform.base.biz.goods.model.goods.req.spu;

import lombok.Data;

import java.util.List;

@Data
public class SpuStateCommand {
    /**
     * 1 上架
     * 0 下架
     */
    private Integer enable;
    /** SPU ID列表 */
    private List<Long> spuIdList;
}