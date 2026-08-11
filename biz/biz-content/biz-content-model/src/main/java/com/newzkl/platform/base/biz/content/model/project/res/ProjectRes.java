package com.newzkl.platform.base.biz.content.model.project.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 项目详情视图对象
 *
 * <p>迁移说明: 对应源 {@code Project}(领域实体兼出参)。省/市/区名称三字段由
 * action 层用区域数据回填, 不落库。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectRes extends BaseRes {

    /**
     * 名称
     */
    private String name;

    /**
     * 简介
     */
    private String desc;

    /**
     * 省编码
     */
    private Integer province;

    /**
     * 省名称 (按 province 编码回填)
     */
    private String provinceName;

    /**
     * 市编码
     */
    private Integer city;

    /**
     * 市名称 (按 city 编码回填)
     */
    private String cityName;

    /**
     * 区编码
     */
    private Integer area;

    /**
     * 区名称 (按 area 编码回填)
     */
    private String areaName;

    /**
     * 合作金额 (Money, 落库 BIGINT 分)
     */
    private Money basicAmount;

    /**
     * 标签列表 (落库为逗号分隔串)
     */
    private List<String> flags;

    /**
     * 详情
     */
    private String detail;

    /**
     * 当前账号是否已表示意向
     */
    private Boolean isInterest;

    /**
     * 视频列表 (按 index 升序)
     */
    private List<ProjectVideoRes> videoList;
}
