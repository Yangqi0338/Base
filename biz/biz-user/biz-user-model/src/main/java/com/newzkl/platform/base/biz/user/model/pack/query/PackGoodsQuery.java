package com.newzkl.platform.base.biz.user.model.pack.query;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 入会礼包商品分页查询
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packgoods.model.query.PackGoodsQuery}。
 * 旧分页参数 {@code current}/{@code size} 改为中台 {@code PageQuery} 的 {@code pageNo}/{@code pageSize}。</p>
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PackGoodsQuery extends PageQuery {

    /**
     * 礼包商品 ID
     */
    private Long id;

    /**
     * 类型（角色 ID）
     */
    private Integer type;

    /**
     * 类型列表
     */
    private List<Integer> typeList;

    /**
     * 礼包等级
     */
    private Integer level;

    /**
     * 礼包名称（模糊匹配）
     */
    private String name;

    /**
     * ID 集合
     */
    private List<Long> idList;

    /**
     * 状态 0 下架 1 上架
     */
    private Integer state;

    /**
     * 是否初始化查询（true 跳过角色分流）
     */
    private Boolean isInit;

    /**
     * 构造：未传排序字段时默认按 update_time 倒序
     */
    public PackGoodsQuery() {
        if (CollUtil.isEmpty(super.getSortField())) {
            super.addDescSortField("update_time");
        }
    }
}
