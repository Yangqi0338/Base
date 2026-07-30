package com.newzkl.platform.base.biz.sys.model.project.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 项目列表视图对象
 *
 * <p>迁移说明: 对应源 {@code ProjectVO}。与 {@code ProjectRes}(详情) 的差异:
 * 列表不返回 {@code detail}/{@code videoList}, 改带 {@code interestNum}(意向人数, 取自缓存计数)
 * 与 {@code coverVideo}(封面视频, 取 index 最小的一条)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectListRes extends BaseRes {

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
     * 合作金额
     */
    private Integer basicAmount;

    /**
     * 标签列表 (落库为逗号分隔串)
     */
    private List<String> flags;

    /**
     * 意向人数 (缓存计数)
     */
    private Integer interestNum;

    /**
     * 当前账号是否已表示意向
     */
    private Boolean isInterest;

    /**
     * 封面视频 (index 最小的一条)
     */
    private ProjectVideoRes coverVideo;
}
