package com.newzkl.platform.base.biz.store.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.enums.store.StoreStyleEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.autotable.annotation.ColumnType;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;

/**
 * 门店样式领域对象
 */
@Data
@TableName
public class StoreStyleDO extends BaseDO {
    
    /**
     * 样式code
     */
    @Index
    private String styleCode;
    
    /**
     * 样式名称
     */
    private String styleName;
    
    /**
     * 主色
     */
    @Index
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
     * 来源模板code
     */
    @Index
    private String sourceCode;

    /**
     * 来源模板名称
     */
    private String sourceName;

    /**
     * 页面类型
     */
    private StoreStyleEnum.PageType pageType;

    /**
     * 样式内容
     */
    @ColumnType(value = MysqlTypeConstant.TEXT)
    private String styleContent;

    /**
     * 商品id集合
     */
    private String goodsIdListStr;

    /**
     * 预览图
     */
    private String previewImage;

}