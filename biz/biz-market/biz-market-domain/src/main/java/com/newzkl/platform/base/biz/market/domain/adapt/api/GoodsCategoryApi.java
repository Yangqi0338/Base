package com.newzkl.platform.base.biz.market.domain.adapt.api;

import java.util.List;

/**
 * 商品域平台分类跨服务出站端口 (outbound port)。
 *
 * <p>迁移: 旧 {@code categorySync} 直接 {@code select id, pid, name, desc, img from category
 * where LEFT(id, 2) = #{rootId}} 读取 goods 域的平台分类表; 中台化后 market 域不得直连
 * 他域表, 须经此端口, 由入口 starter 侧远程 consumer 覆盖默认实现。</p>
 *
 * @author KC
 */
public interface GoodsCategoryApi {

    /**
     * 查询指定根节点下的平台分类全量扁平列表。
     *
     * @param rootId 根分类ID (旧实现按 id 前缀匹配子树)
     * @return 扁平分类列表, 恒非 null; 无数据时为空集合
     */
    List<PlatformCategoryInfo> platformCategoryTree(Long rootId);
}
