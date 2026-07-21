package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 持久化的合计Map，用于存储和返回合计数据
 *
 * @author muc_fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizCountMap extends ArrayList<Map<String, Object>> implements Serializable {

    private static final String COUNT = "count";

    public Long getCount(int row) {
        List<Long> numList = CollUtil.map(this, it ->
                MapUtil.getLong(it, COUNT + row, 0L), true);
        return numList.stream().mapToLong(Long::longValue).sum();
    }

}
