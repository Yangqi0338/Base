package com.newzkl.platform.base.biz.order.domain.service.hdh.huidinghuo.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量获取商品信息请求类（支持分页和筛选）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoProductBatchGetReq extends HuiDingHuoBaseReq {

    /**
     * 页数（必填，从1开始）
     */
    @NotNull(message = "页数不能为空")
    private Integer page = 1;

    /**
     * 每页数量（必填，建议不超过100）
     */
    @NotNull(message = "每页数量不能为空")
    private Integer limit = 10;

    /**
     * 日志编号（可选，用于问题排查）
     */
    private String logNo;

    /**
     * 上一页最后一个商品ID（可选，用于分页续查，优化翻页性能）
     */
    private String prePageLastId;

    /**
     * 关键词（可选，用于搜索商品名称/描述）
     */
    private String keyword;

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
     * 品牌ID集合（可选，用于筛选指定品牌商品）
     */
    private List<Long> brandIds;

    /**
     * 发货方式集合（可选，用于筛选支持指定发货方式的商品）
     */
    private List<Integer> channelTypes;
}