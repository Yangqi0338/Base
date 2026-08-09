package com.newzkl.platform.base.biz.goods.model.goods.req.goodsZone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 商品分组新增/编辑请求
 * @author sijiwang
 * @since 2026-03-18
 */
@Data
public class GoodsZoneAddReq {

    /**
     * 主键ID（编辑时必填）
     */
    private Long id;

    /**
     * 分组名称（必填，最多50字）
     */
    @NotBlank(message = "分组名称不能为空")
    @Size(max = 50, message = "分组名称不能超过50个字")
    private String groupName;

    /**
     * 分组描述
     */
    private String groupDesc;

    /**
     * 背景图
     */
    private String backgroundImg;

    /**
     * 排序类型
     * @ext 1-默认 2-销量从高到低 3-上架时间倒序
     */
    @NotNull(message = "排序类型不能为空")
    private Integer sortType;

    /**
     * 搜索框显示状态
     * @ext 0-不显示 1-显示
     */
    @NotNull(message = "搜索框显示状态不能为空")
    private Integer searchBoxStatus;

    /**
     * 价格显示状态
     * @ext 0-不显示 1-显示
     */
    @NotNull(message = "价格显示状态不能为空")
    private Integer priceShowStatus;

    /**
     * 门店显示状态
     * @ext 0-不显示 1-显示
     */
    @NotNull(message = "门店显示状态不能为空")
    private Integer storeShowStatus;

    /**
     * 分组状态
     * @ext 0-禁用 1-启用
     */
    @NotNull(message = "分组状态不能为空")
    private Integer state;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 创建人ID（系统填充）
     */
    private Long createId;
}