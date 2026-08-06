package com.newzkl.platform.base.biz.sys.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.OcrApi;
import com.newzkl.platform.base.biz.sys.domain.service.RegionDomain;
import com.newzkl.platform.base.biz.sys.model.region.req.RegionReq;
import com.newzkl.platform.base.biz.sys.model.region.vo.Area;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 行政区域领域服务实现
 *
 * <p>区域数据来源于 classpath 下 {@code area.json}。</p>
 *
 * <p>迁移说明: {@link RegionDomainImpl#getBusinessRegion} 已补迁, 但源实现里 {@code operatorFilter=1} 的
 * 运营商区域打标分支依赖 operatorFacade 跨域 RPC, Base 无对应出站端口, 该分支保留为
 * 注释 + {@code TODO[cross-service]}, 未硬接。其余分支 (父编码过滤 / 名称匹配打标 /
 * 剔除打标 / 平展) 与源逐字等价。</p>
 *
 * @author fang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegionDomainImpl implements RegionDomain {

    private static final String AREA_JSON = "area.json";

    private final OcrApi ocrApi;

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
            throw new PlatformException(BaseErrorCode.PARAM);
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

    @Override
    public List<Area> getBusinessRegion(RegionReq req) {
        List<Area> source = readAreaList();

        Integer parentCode = req.getParentCode();
        if (parentCode != null) {
            source = Area.findPlattenList(source, parentCode);
        }

        Consumer<Area> enableFlagConsumer = area -> area.setFlag(1);
        Map<Integer, Consumer<Area>> doBusinessMap = new HashMap<>();

        // TODO[cross-service]: operatorFilter=1 时, 源实现经 operatorFacade 查运营商已开通区域并逐个打标:
        //   if (NumberUtil.equals(req.getOperatorFilter(), (Integer) 1)) {
        //       List<Long> codeList = operatorFacade.operatorTypeThirdIdGroupCountQuery(
        //               OperatorEnum.DataType.AREA.getCode(), req.getMatchStrList());
        //       if (CollUtil.isNotEmpty(codeList)) {
        //           codeList.forEach(code -> doBusinessMap.put(code.intValue(), enableFlagConsumer));
        //       }
        //   }
        // Base 尚无运营商域出站端口, 硬接会静默丢标记, 故整段保留注释不实现。
        // 待运营商能力接入后, 在此改为经 domain/adapt/api 下的运营商端口取 codeList。
        if (NumberUtil.equals(req.getOperatorFilter(), (Integer) 1)) {
            log.warn("[RegionDomainImpl] operatorFilter=1 但运营商区域打标能力缺失, 本次返回未按运营商打标的区域树");
        }

        List<String> matchStrList = req.getMatchStrList();
        if (CollUtil.isNotEmpty(matchStrList)) {
            Map<Integer, Area> matched = findAreaByCondition(source, matchStrList);
            matched.forEach((code, area) -> doBusinessMap.put(code, enableFlagConsumer));
        }
        applyBusiness(source, doBusinessMap);

        if (NumberUtil.equals(req.getFlagRemove(), (Integer) 1)) {
            source = removeFlagAreaList(source);
        }

        if (req.getFlatten() == Boolean.TRUE) {
            source = Area.findPlattenList(source, null);
        }

        return source;
    }

    @Override
    public Object businessIdentify(String imageUrl) {
        Object recognized = ocrApi.recognizeBusinessLicense(imageUrl);
        if (recognized == null) {
            return null;
        }
        JSONObject response = JSONUtil.parseObj(JSONUtil.toJsonStr(recognized));
        JSONObject result = response.getJSONObject("result");
        if (result == null) {
            return response;
        }
        String address = result.getStr("address");
        if (StrUtil.isBlank(address)) {
            return response;
        }
        loadIfAbsent();
        List<Integer> codeList = new ArrayList<>();
        for (Area area : areaList) {
            area.getCodeByName(codeList, address);
        }
        if (CollUtil.isNotEmpty(codeList)) {
            codeList = CollUtil.reverse(codeList);
            for (Integer code : codeList) {
                Area area = areaMap.get(code);
                if (area != null && StrUtil.isNotBlank(area.getName())) {
                    address = address.replace(area.getName(), StrUtil.EMPTY);
                }
            }
            result.set("address", address);
            response.set("areaCode", codeList);
        }
        return response;
    }

    /**
     * 按名称匹配串收集命中的区域
     *
     * <p>匹配方向与源一致: 判断 {@code matchStr} 是否包含区域名称, 而非反向包含</p>
     *
     * @param areaList     区域列表
     * @param matchStrList 名称匹配串列表
     * @return 命中的 编码-区域 映射
     */
    private Map<Integer, Area> findAreaByCondition(List<Area> areaList, List<String> matchStrList) {
        Map<Integer, Area> matchMap = new HashMap<>();
        for (Area area : areaList) {
            boolean hit = matchStrList.stream().anyMatch(matchStr -> StrUtil.contains(matchStr, area.getName()));
            if (hit) {
                matchMap.put(area.getCode(), area);
            }
        }
        return matchMap;
    }

    /**
     * 递归对命中的区域执行打标动作
     *
     * @param areaList      区域列表
     * @param doBusinessMap 编码到打标动作的映射
     */
    private void applyBusiness(List<Area> areaList, Map<Integer, Consumer<Area>> doBusinessMap) {
        for (Area area : areaList) {
            Consumer<Area> consumer = doBusinessMap.get(area.getCode());
            if (consumer != null) {
                consumer.accept(area);
            }
            if (ObjectUtil.isNotEmpty(area.getChildren())) {
                applyBusiness(area.getChildren(), doBusinessMap);
            }
        }
    }

    /**
     * 递归剔除已打标 (flag=1) 的区域, 子节点全被剔除的父节点一并剔除
     *
     * @param areaList 区域列表
     * @return 剔除后的区域列表
     */
    private List<Area> removeFlagAreaList(List<Area> areaList) {
        List<Area> newList = new ArrayList<>();
        for (Area area : areaList) {
            if (NumberUtil.equals(area.getFlag(), (Integer) 1)) {
                continue;
            }
            if (CollUtil.isEmpty(area.getChildren())) {
                newList.add(area);
            } else {
                List<Area> children = removeFlagAreaList(area.getChildren());
                area.setChildren(children);
                if (CollUtil.isNotEmpty(children)) {
                    newList.add(area);
                }
            }
        }
        return newList;
    }

    /**
     * 首次访问时懒加载区域树并构建编码索引
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
     * 从 classpath 读取区域树
     *
     * @return 区域列表
     */
    private List<Area> readAreaList() {
        try (InputStream in = new ClassPathResource(AREA_JSON).getInputStream()) {
            String data = IoUtil.read(in, StandardCharsets.UTF_8);
            return JSONUtil.toList(data, Area.class);
        } catch (Exception e) {
            throw new PlatformException(BaseErrorCode.SERVER);
        }
    }

    /**
     * 递归构建编码到区域的索引
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
