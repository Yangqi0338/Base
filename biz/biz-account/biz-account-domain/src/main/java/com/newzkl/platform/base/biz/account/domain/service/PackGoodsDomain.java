package com.newzkl.platform.base.biz.account.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.pack.query.PackGoodsQuery;
import com.newzkl.platform.base.biz.account.model.pack.req.PackGoodsCommand;
import com.newzkl.platform.base.biz.account.model.pack.res.PackGoodsRes;

import java.util.List;

/**
 * 入会礼包商品领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.packgoods.service.PackGoodsDomainService}。</p>
 *
 * @author KC
 */
public interface PackGoodsDomain {

    /**
     * 新增或更新礼包商品（{@code command.id} 为空新增，否则更新）
     *
     * @param command 写入入参
     * @return 礼包主键ID
     */
    Long packGoodsSave(PackGoodsCommand command);

    /**
     * 按ID批量物理删除礼包商品
     *
     * @param idList 主键ID集合
     */
    void packGoodsDelete(List<Long> idList);

    /**
     * 按ID查询礼包商品详情
     *
     * @param id 主键ID
     * @return 礼包出参
     */
    PackGoodsRes packGoodsVO(Long id);

    /**
     * 分页查询礼包商品（按角色分流回填 canBuy）
     *
     * @param query 分页查询
     * @return 礼包分页
     */
    Page<PackGoodsRes> packGoodsVOList(PackGoodsQuery query);

    /**
     * 按类型与等级查询礼包商品
     *
     * @param type  类型（角色 ID）
     * @param level 礼包等级
     * @return 礼包出参，不存在返回 null
     */
    PackGoodsRes findByTypeAndLevel(Integer type, Integer level);
}
