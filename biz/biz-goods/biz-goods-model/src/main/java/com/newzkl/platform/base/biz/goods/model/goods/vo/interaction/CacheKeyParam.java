package com.newzkl.platform.base.biz.goods.model.goods.vo.interaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存Key解析后的参数封装
 * 
 * @author sijiwang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheKeyParam {
    private Long storeId;
    
    private String targetType;
    
    private Long targetId;
    
    private String targetTypeCode;
}