package com.newzkl.platform.base.biz.goods.infrastructure.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 视频-商品关联数据对象。
 *
 * <p>纯关系表, 无主键与审计列, 故不继承 {@code BaseDO} (照 new-scm)。</p>
 *
 * @author KC
 */
@Data
@TableName("video_spu_relation")
public class VideoSpuRelationDO implements Serializable {

    /**
     * 视频 ID。
     */
    private Long videoId;

    /**
     * 商品 SPU ID。
     */
    private Long spuId;

    /**
     * 视频类型: 1 短视频, 2 长视频。
     */
    private Integer type;
}
