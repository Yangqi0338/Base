package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.functions.T;

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
                BizCountMap.getCount(it,row), true);
        return numList.stream().mapToLong(Long::longValue).sum();
    }

    public static Long getCount(Map<String, Object> map, int row) {
        return MapUtil.getLong(map, COUNT + row, 0L);
    }

    public static Integer getIntCount(Map<String, Object> map, int row) {
        return MapUtil.getInt(map, COUNT + row, 0);
    }

}
