package com.newzkl.platform.base.biz.goods.model.goods.vo.video;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 商品-短视频视图对象
 *
 * <p>迁移偏离: new-scm 用泛型 {@code Video}/{@code VideoVO} 基类支撑长/短视频两型,
 * 本轮仅短视频入 scope, 故扁平化为独立视图, 不引入基类 (YAGNI)。
 * SPU 简要字段 (code/name/img) 由列表查询 join {@code spu} 表带出。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShortVideoVO extends BaseRes {

    /**
     * 视频路径
     */
    private String path;

    /**
     * 封面路径
     */
    private String coverPath;

    /**
     * 关联 SPU ID (列表查询取第一个关联)
     */
    private Long spuId;

    /**
     * 关联 SPU 编码
     */
    private String code;

    /**
     * 关联 SPU 名称
     */
    private String name;

    /**
     * 关联 SPU 图片
     */
    private String img;

    /**
     * 关联 SPU ID 列表 (详情查询填充)
     */
    private List<Long> spuIdList;
}
