package com.newzkl.platform.base.biz.goods.model.goods.query.video;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 商品-短视频分页查询。
 *
 * <p>{@code idList} / {@code accountId} / {@code createStartTime} / {@code createEndTime}
 * 由 {@link BizPageQuery} 提供 (创建时间为 {@code createTime} 数组的首尾投影)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShortVideoQuery extends BizPageQuery {

    /**
     * 关联 SPU 名称 (模糊)。
     */
    private String spuName;

    /**
     * 关联 SPU ID 列表。
     */
    private List<Long> spuIdList;
}
