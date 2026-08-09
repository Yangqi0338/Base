package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品-短视频数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class ShortVideoDO extends BaseDO {

    /**
     * 视频路径
     */
    private String path;

    /**
     * 封面路径
     */
    private String coverPath;
}
