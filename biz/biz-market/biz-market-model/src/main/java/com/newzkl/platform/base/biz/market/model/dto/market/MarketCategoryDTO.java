package com.newzkl.platform.base.biz.market.model.dto.market;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 
 * 市场分类
 */
@Data
public class MarketCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 市场数量
     */
    private Integer marketNum;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}