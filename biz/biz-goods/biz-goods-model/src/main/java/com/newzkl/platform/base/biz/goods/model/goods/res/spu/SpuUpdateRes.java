package com.newzkl.platform.base.biz.goods.model.goods.res.spu;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2514:31
 */
@Data
public class SpuUpdateRes {
    /** 新增SKU ID列表 */
    private List<Long> addSkuIdList;
    /** 删除SKU ID列表 */
    private List<Long> deleteSkuIdList;
    /** 更新SKU ID列表 */
    private List<Long> updateSkuIdList;

    public SpuUpdateRes(){
        this.addSkuIdList = new ArrayList<>();
        this.deleteSkuIdList = new ArrayList<>();
        this.updateSkuIdList = new ArrayList<>();
    }
}
