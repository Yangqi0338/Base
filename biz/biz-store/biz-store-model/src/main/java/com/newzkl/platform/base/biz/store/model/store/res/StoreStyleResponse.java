package com.newzkl.platform.base.biz.store.model.store.res;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 门店样式领域对象
 */
@Data
public class StoreStyleResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 样式code
     */
    private String styleCode;
    
    /**
     * 样式名称
     */
    private String styleName;
    
    /**
     * 主色
     */
    private String essentialColour;
    
    /**
     * 辅色
     */
    private String auxiliaryColor;
    
    /**
     * 使用门店数
     */
    private Integer useStoreNum;
    
    /**
     * 描述
     */
    private String packageDescribe;
    
    /**
     * 类型：1 默认
     */
    private Integer type;
    
    /**
     * 状态：0 禁用,1 启用
     */
    private Integer state;
    
    /**
     * 状态：0 正常,1 已删除
     */
    private Integer deleted;
    
    /**
     * 创建人id
     */
    private Long createId;
    
    /**
     * 创建人名
     */
    private String createName;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 修改人id
     */
    private Long menderId;
    
    /**
     * 修改人名
     */
    private String menderName;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 样式内容
     */
    private String styleContent;

    /**
     * 商品id集合
     */
    private String goodsIdListStr;

    /**
     * 预览图
     */
    private String previewImage;

    /**
     * 使用数
     */
    private Integer useNum;

}