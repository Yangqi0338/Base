package com.newzkl.platform.base.biz.goods.model.goods.res.report;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuSimpleVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 报告实体
 *
 * @author kc
 */
@Data
public class ReportRes implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private String path;
    /**
     * 分类id列表
     */
    private List<Long> categoryIdList;
    /**
     * 商品id列表
     */
    private List<Long> spuIdList;
    /**
     * 商品列表
     */
    private List<SpuSimpleVO> spuList;

}