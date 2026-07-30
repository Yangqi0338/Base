package com.newzkl.platform.base.biz.goods.domain.goodPackage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.goodPackage.GoodPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodPackage.GoodPackageReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.goodPackage.GoodPackageVO;

/**
 * 商品套餐领域服务
 *
 * @author KC
 */
public interface GoodPackageDomain {

    /**
     * 创建套餐 (套餐业务编码查重)
     *
     * @param req 套餐请求
     * @return 套餐主键 ID
     */
    Long create(GoodPackageReq req);

    /**
     * 更新套餐 (存在性校验)
     *
     * @param req 套餐请求 (id 必填)
     */
    void update(GoodPackageReq req);

    /**
     * 启用套餐
     *
     * @param id 套餐主键 ID
     */
    void enable(Long id);

    /**
     * 停用套餐
     *
     * @param id 套餐主键 ID
     */
    void disable(Long id);

    /**
     * 按套餐业务编码查详情
     *
     * @param packageId 套餐业务编码
     * @return 套餐视图对象
     */
    GoodPackageVO detailByPackageId(String packageId);

    /**
     * 套餐分页
     *
     * @param query 套餐查询
     * @return 套餐分页
     */
    Page<GoodPackageVO> page(GoodPackageQuery query);
}
