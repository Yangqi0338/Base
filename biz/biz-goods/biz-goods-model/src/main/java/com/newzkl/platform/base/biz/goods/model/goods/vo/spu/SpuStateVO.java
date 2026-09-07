package com.newzkl.platform.base.biz.goods.model.goods.vo.spu;

import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品spu信息
 *
 * @author wqm
 * @since 2023年04月27日 14:58:00
 */
@Data
public class SpuStateVO implements Serializable {
    /**
     * spuId
     */
    private Long id;
    /**
     * 状态 0:仓库中 2:上架中 3:待上架 (查询)
     */
    private SpuEnum.State state;
}
