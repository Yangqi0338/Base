package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 测试报告
 *
 * @author kc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class ReportDO extends BaseDO {

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
    @Index
    private String categoryIdList;
    /**
     * 商品id列表
     */
    @Index
    private String spuIdList;
}