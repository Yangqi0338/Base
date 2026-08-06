package com.newzkl.platform.base.common.ddd.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.dto.ExecutorDTO;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础转化实现
 *
 * @author kc
 */
@Component
public class BaseConvert {

    @Named("str2List")
    public List<String> str2List(String str) {
        return StrUtil.split(str, ",");
    }

    @Named("list2Str")
    public String list2Str(List<String> strList) {
        return CollUtil.join(strList, ",");
    }

    @Named("toJson")
    public String toJson(Object obj) {
        return JSONUtil.toJsonStr(obj);
    }

    @Named("getClass")
    public Class<?> getClass(Object obj) {
        if (obj == null) return null;
        return obj.getClass();
    }

    @Named("getClassName")
    public String getClassName(Object obj) {
        if (obj == null) return null;
        return obj.getClass().getCanonicalName();
    }

    /**
     * Map → JSON 字符串（用于领域模型的shipVO Map → DO的JSON字符串）
     */
    @Named("mapToJson")
    public String mapToJson(Map<String, Object> shipMap) {
        if (shipMap == null || shipMap.isEmpty()) {
            return "{}"; // 默认空JSON
        }
        return JSONUtil.toJsonStr(shipMap);
    }

    /**
     * JSON 字符串 → Map（用于DO的shipVO JSON → 领域模型的Map）
     */
    @Named("jsonToMap")
    public Map<String, Object> jsonToMap(String shipJson) {
        if (StringUtils.isEmpty(shipJson) || "{}".equals(shipJson)) {
            return new HashMap<>(); // 默认空Map
        }
        return JSONUtil.toBean(shipJson, Map.class);
    }

    /**
     * 平铺出参 → DO 的嵌套 executor
     *
     * <p>反向组装, 将 Res 的平铺操作人字段回收进 ExecutorDTO</p>
     */
    @Named("resToExecutor")
    public ExecutorDTO resToExecutor(BaseRes src) {
        if (src == null) {
            return null;
        }
        ExecutorDTO executor = new ExecutorDTO();
        executor.setCreatorName(src.getCreatorName());
        executor.setUpdater(src.getUpdater());
        executor.setUpdaterName(src.getUpdaterName());
        return executor;
    }

    /**
     * 平铺 DTO → DO 的嵌套 executor
     *
     * <p>反向组装, 将 DTO 的平铺操作人字段回收进 ExecutorDTO</p>
     */
    @Named("dtoToExecutor")
    public ExecutorDTO dtoToExecutor(BaseDTO src) {
        if (src == null) {
            return null;
        }
        ExecutorDTO executor = new ExecutorDTO();
        executor.setCreatorName(src.getCreatorName());
        executor.setUpdater(src.getUpdater());
        executor.setUpdaterName(src.getUpdaterName());
        return executor;
    }

}