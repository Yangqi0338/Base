package com.newzkl.platform.base.biz.goods.infrastructure.support;

import lombok.Data;

/**
 * 基础数据实体类
 *
 * @author god
 */
@Data
public abstract class CategoryLayerDO extends CategoryBaseDO {
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 祖先路径+自身ID，逗号分隔，逗号结尾
     * 根节点为 "id,"，如 "11,"
     * 子节点为 "父pidList + 自身id + ,"，如 "11,1101,"
     */
    private String pidList;
    /**
     * 层级深度，根节点为1
     */
    private Integer level;
    /**
     * 已绑定的品牌 ID 集合, 逗号分隔无尾逗号 (如 "11,12,13")
     *
     * <p>迁移自 new-scm category 表的 brand_id_list 列 (Base 建表时曾删, 本轮 bindBrand 端点补迁时恢复)。
     * 整存整取, 无 like 查询, 故不加尾逗号, 与 industry.category_id_list 同格式 ({@code BizUtil#addIdString})</p>
     */
    private String brandIdList;
}
