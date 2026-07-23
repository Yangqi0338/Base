package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 
 * 装修页面
 */
@Data
public class FitmentPageDO implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 页面类型 1：平台商品 2：平台+自营
     */
    private Integer pageType;

    /**
     * 内容
     */
    private String content;

    /**
     * 商品集合
     */
    private String goodsId;

    /**
     * 市场集合
     */
    private String markets;

    /**
     * kv
     */
    private String kv;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}