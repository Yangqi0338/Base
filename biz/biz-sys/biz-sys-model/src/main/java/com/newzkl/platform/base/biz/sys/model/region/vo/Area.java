package com.newzkl.platform.base.biz.sys.model.region.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 行政区域节点
 *
 * @author muc_fang
 */
@Data
public class Area {
    /**
     * 子区域
     */
    private List<Area> children;
    /**
     * 区域编码
     */
    private Integer code;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 名称
     */
    private String name;
    /**
     * 父编码
     */
    private Integer pid;
    /**
     * 标记
     */
    private Integer flag;

    /**
     * 将树形区域平铺, 并按父编码过滤
     *
     * @param areaList   区域树
     * @param parentCode 父编码, 为 null 时不过滤
     * @return 平铺后的区域列表
     */
    public static List<Area> findPlattenList(List<Area> areaList, Integer parentCode) {
        List<Area> resultList = new ArrayList<>();
        areaList.forEach(area -> transform(resultList, area));
        return resultList.stream().filter(area ->
                parentCode == null || area.getPid().equals(parentCode)
        ).collect(Collectors.toList());
    }

    /**
     * 递归平铺区域节点
     *
     * @param areaList 结果列表
     * @param area     当前节点
     */
    public static void transform(List<Area> areaList, Area area) {
        areaList.add(area);
        List<Area> childrenList = area.getChildren();
        if (CollUtil.isNotEmpty(childrenList)) {
            area.setChildren(null);
            childrenList.forEach(child -> transform(areaList, child));
        }
    }

    /**
     * 按名称匹配收集区域编码 (最多 3 级)
     *
     * @param codeList 编码收集列表
     * @param nameStr  待匹配名称串
     */
    public void getCodeByName(List<Integer> codeList, String nameStr) {
        if (StrUtil.isNotBlank(nameStr) && codeList.size() < 3) {
            String trimName = StrUtil.removeSuffix(StrUtil.removeSuffix(StrUtil.removeSuffix(this.getName(), "省"), "市"), "区");
            boolean isFind = false;
            if (StrUtil.length(trimName) >= 2) {
                isFind = StrUtil.containsAny(nameStr, this.getName(), trimName);
            }else {
                isFind = StrUtil.contains(nameStr, this.getName());
            }

            if (CollUtil.isNotEmpty(this.getChildren())) {
                this.getChildren().forEach(child -> child.getCodeByName(codeList, nameStr));
            }
            if (isFind) {
                codeList.add(this.getCode());
            }
        }
    }
}
