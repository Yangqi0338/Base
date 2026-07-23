package com.newzkl.platform.base.biz.goods.model.goods.vo.report;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报告视图对象
 *
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReportVO extends BaseRes {

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
    private String categoryIdList;
    /**
     * 商品id列表
     */
    private String spuIdList;

}