package com.newzkl.platform.base.biz.goods.model.goods.query.brand;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;

import java.util.List;

/**
 * 分类
 *
 * @author fang
 */
@Data
public class PalletCategoryPageQuery extends BizPageQuery {

    /**
     * 父ID
     */
    private Long pid;
    /**
     * 用户ID或者平台ID
     */
    private Long accountIdOrAdmin;
    /**
     * ID或父ID
     */
    private Long idOrPid;
    /**
     * 父ID集合, 可查询出所有子集
     */
    private List<Long> idWithChild;
    /**
     * 父ID集合
     */
    private List<Long> pidList;
    /**
     * 名称查询
     */
    private String name;
    /**
     * 品牌名称
     */
    private String brandName;
    private Long notId;
    /**
     * 行业ID
     */
    private Long industryId;
}
