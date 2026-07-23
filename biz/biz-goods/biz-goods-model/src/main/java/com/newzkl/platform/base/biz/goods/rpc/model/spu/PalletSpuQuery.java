package com.newzkl.platform.base.biz.goods.rpc.model.spu;

import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * spu
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PalletSpuQuery extends BusinessPageQuery implements Serializable {

    /**
     * 商品渠道来源 SpuAccountId
     * PLATFORM(0,"平台"),
     * YYT(1,"怡亚通"),
     * HDH(2,"会订货"),
     */
    @NotNull(message = "商品渠道来源不能为空")
    private Integer spuChannelSource;

    /**
     * 外部商品id
     */
    private String outSpuId;

    /**
     * 名称 (查询)
     */
    private String name;

    /**
     * 一级类目ID（可选，用于筛选指定类目商品）
     */
    private Long cateId1;

    /**
     * 二级类目ID（可选，需配合一级类目ID使用）
     */
    private Long cateId2;

    /**
     * 三级类目ID（可选，需配合二级类目ID使用）
     */
    private Long cateId3;

    /**
     * 发货方式集合（可选，用于筛选支持指定发货方式的商品）
     */
    private List<Integer> channelTypes;

}
