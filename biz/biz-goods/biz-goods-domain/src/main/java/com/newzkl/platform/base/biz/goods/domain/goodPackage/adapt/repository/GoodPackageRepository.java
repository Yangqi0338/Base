package com.newzkl.platform.base.biz.goods.domain.goodPackage.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.goodPackage.GoodPackageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.goodPackage.GoodPackageReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.goodPackage.GoodPackageVO;

/**
 * 商品套餐仓储端口
 *
 * @author KC
 */
public interface GoodPackageRepository {

    /**
     * 套餐新增/更新
     *
     * @param req 套餐请求
     * @return 套餐主键 ID
     */
    Long save(GoodPackageReq req);

    /**
     * 按主键查套餐
     *
     * @param id 套餐主键 ID
     * @return 套餐视图对象, 不存在返回 null
     */
    GoodPackageVO findById(Long id);

    /**
     * 按套餐业务编码查套餐
     *
     * @param packageId 套餐业务编码
     * @return 套餐视图对象, 不存在返回 null
     */
    GoodPackageVO findByPackageId(String packageId);

    /**
     * 套餐业务编码是否已存在
     *
     * @param packageId 套餐业务编码
     * @return 存在返回 true
     */
    boolean existsByPackageId(String packageId);

    /**
     * 更新套餐状态
     *
     * @param id    套餐主键 ID
     * @param state 状态: 1 启用, 0 停用
     */
    void updateState(Long id, Integer state);

    /**
     * 套餐分页
     *
     * @param query 套餐查询
     * @return 套餐分页
     */
    Page<GoodPackageVO> page(GoodPackageQuery query);
}
