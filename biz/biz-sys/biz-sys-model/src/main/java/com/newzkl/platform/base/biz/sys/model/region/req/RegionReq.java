package com.newzkl.platform.base.biz.sys.model.region.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 区域业务查询入参
 *
 * <p>迁移说明: 源 {@code com.zkl.scm.admin.domain.other.model.req.RegionReq}。字段名逐字沿用,
 * 前端契约不变。父类 {@code PageQuery} 沿用旧继承关系, 但本查询实际不分页 (区域树整棵返回)</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegionReq extends PageQuery {

    /**
     * 父级区域编码, 传入时按该父编码平铺过滤
     */
    private Integer parentCode;

    /**
     * 是否按运营商已开通区域打标
     *
     * @ext 取值: 1=打标
     */
    private Integer operatorFilter;

    /**
     * 是否剔除已打标区域
     *
     * @ext 取值: 1=剔除
     */
    private Integer flagRemove;

    /**
     * 名称匹配串列表, 命中的区域会被打标
     */
    private List<String> matchStrList;

    /**
     * 是否平展返回
     */
    private Boolean flatten;
}
