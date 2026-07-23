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
    private List<Long> addSkuIdList;
    private List<Long> deleteSkuIdList;
    private List<Long> updateSkuIdList;

    public SpuUpdateRes(){
        this.addSkuIdList = new ArrayList<>();
        this.deleteSkuIdList = new ArrayList<>();
        this.updateSkuIdList = new ArrayList<>();
    }
}
