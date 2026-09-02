package com.newzkl.platform.base.biz.goods.model.goods.query.spu;

import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;

/**
* spu属性
* @author fang
*/
@Data
public class SpuAttributeQuery extends BizPageQuery {

    /**
    * spuId (查询)
    */
    private Long spuId;
    /**
     * SPU_ID集合
     */
    private List<Long> spuIdList;

    public void setSpuId(Long spuId) {
        this.spuIdList = doWrapperList(spuIdList, spuId);
    }
    /**
    * 类型 (查询)
    * @ext 0:销售属性 1:参数属性
    */
    private SpuEnum.SpuAttributeType type;
}
