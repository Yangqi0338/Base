package com.newzkl.platform.base.biz.sys.domain.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.sys.domain.service.RegionDomain;
import com.newzkl.platform.base.biz.sys.model.region.vo.Area;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行政区域领域服务实现。
 *
 * <p>区域数据来源于 classpath 下 {@code area.json} (随 infrastructure 打包)。</p>
 *
 * <p>迁移说明: 源 {@code getBusinessRegion} 依赖 operatorFacade (跨域 RPC) 做运营商区域筛选,
 * 属跨域编排, 暂缓 (见 sys 迁移计划)。此处仅保留基础区域读取/平铺能力。</p>
 *
 * @author fang
 */
@Service
public class RegionDomainImpl implements RegionDomain {

    private static final String AREA_JSON = "area.json";

    private volatile List<Area> areaList = new ArrayList<>();
    private final Map<Integer, Area> areaMap = new HashMap<>();

    @Override
    public String getRegion() {
        loadIfAbsent();
        return JSONUtil.toJsonStr(areaList);
    }

    @Override
    public String getRegionByCode(Integer code) {
        loadIfAbsent();
        Area area = areaMap.get(code);
        if (area == null) {
            throw new ScmException(BaseErrorCode.PARAM);
        }
        return area.getName();
    }

    @Override
    public List<Area> getRegionList(Integer parentCode, boolean flatten) {
        List<Area> source = readAreaList();
        if (parentCode != null) {
            source = Area.findPlattenList(source, parentCode);
        } else if (flatten) {
            source = Area.findPlattenList(source, null);
        }
        return source;
    }

    /**
     * 首次访问时懒加载区域树并构建编码索引。
     */
    private synchronized void loadIfAbsent() {
        if (ObjectUtil.isNotEmpty(areaList)) {
            return;
        }
        List<Area> loaded = readAreaList();
        Map<Integer, Area> index = new HashMap<>();
        indexArea(index, loaded);
        this.areaList = loaded;
        this.areaMap.putAll(index);
    }

    /**
     * 从 classpath 读取区域树。
     *
     * @return 区域列表
     */
    private List<Area> readAreaList() {
        try (InputStream in = new ClassPathResource(AREA_JSON).getInputStream()) {
            String data = IoUtil.read(in, StandardCharsets.UTF_8);
            return JSONUtil.toList(data, Area.class);
        } catch (Exception e) {
            throw new ScmException(BaseErrorCode.SERVER);
        }
    }

    /**
     * 递归构建编码到区域的索引。
     *
     * @param index 索引 map
     * @param areas 区域列表
     */
    private void indexArea(Map<Integer, Area> index, List<Area> areas) {
        for (Area area : areas) {
            index.put(area.getCode(), area);
            if (ObjectUtil.isNotEmpty(area.getChildren())) {
                indexArea(index, area.getChildren());
            }
        }
    }
}
