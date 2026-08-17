package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
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
                BizCountMap.getCount(it,row), true);
        return numList.stream().mapToLong(Long::longValue).sum();
    }

    public static Long getCount(Map<String, Object> map, int row) {
        return MapUtil.getLong(map, COUNT + row, 0L);
    }

    public static Integer getIntCount(Map<String, Object> map, int row) {
        return MapUtil.getInt(map, COUNT + row, 0);
    }

    public BizCountMap camelKeyCountMap(){
        this.forEach((map)-> {
            map.forEach((k,v)-> {
                if (!StrUtil.containsIgnoreCase(k,"count")) {
                    MapUtil.renameKey(map, k, StrUtil.toCamelCase(k));
                }
            });
        });
        return this;
    }

    public <T> List<T> toList(Class<T> clazz){
        return TransferUtils.transfers(this, clazz);
    }
}
