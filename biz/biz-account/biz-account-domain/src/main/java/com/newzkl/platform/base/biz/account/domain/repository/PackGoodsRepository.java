package com.newzkl.platform.base.biz.account.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.pack.query.PackGoodsQuery;
import com.newzkl.platform.base.biz.account.model.pack.res.PackGoodsRes;

import java.util.List;

/**
 * 入会礼包商品仓储端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packgoods.repository.PackGoodsRepository}。</p>
 *
 * <p>迁移说明：旧接口收发 {@code PackGoodsDO}/{@code PackGoodsVO}，中台按防腐约定收发
 * {@code PackGoodsRes}（域内 DTO）；分页返回 {@code Page} 直返（前端契约 list→records）。</p>
 *
 * @author KC
 */
public interface PackGoodsRepository {

    /**
     * 新增礼包商品
     *
     * @param res 礼包数据
     * @return 新增后的礼包
     */
    PackGoodsRes save(PackGoodsRes res);

    /**
     * 按ID更新礼包商品
     *
     * @param res 礼包数据
     * @return 更新后的礼包
     */
    PackGoodsRes updateById(PackGoodsRes res);

    /**
     * 按ID批量物理删除礼包商品
     *
     * @param idList 主键ID集合
     * @return 删除条数
     */
    int deleteBatch(List<Long> idList);

    /**
     * 按ID查询礼包商品
     *
     * @param id 主键ID
     * @return 礼包，不存在返回 null
     */
    PackGoodsRes getById(Long id);

    /**
     * 分页查询礼包商品
     *
     * @param query 分页查询
     * @return 礼包分页
     */
    Page<PackGoodsRes> pageQuery(PackGoodsQuery query);
}
