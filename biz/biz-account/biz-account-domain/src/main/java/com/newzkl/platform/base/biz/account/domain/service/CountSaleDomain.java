package com.newzkl.platform.base.biz.account.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.CountSaleQuery;
import com.newzkl.platform.base.biz.account.model.req.CountSaleReq;
import com.newzkl.platform.base.biz.account.model.res.CountSaleVO;

import java.util.List;

/**
 * 销售统计领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.count.service.ICountSaleDomain}。
 * 旧 {@code countSaleEdit(List<EditColumnDTO>, Long)} 列自增能力仅供统计定时任务使用,
 * 无本轮端点调用, 未随本切片迁移。</p>
 *
 * @author KC
 */
public interface CountSaleDomain {

    /**
     * 新建销售统计
     *
     * @param req 销售统计入参
     * @return 主键 ID
     */
    Long save(CountSaleReq req);

    /**
     * 修改销售统计
     *
     * <p>以入参 {@code id} 为更新目标, 与旧 {@code countSaleEdit(Long, CountSaleCommand)} 一致。</p>
     *
     * @param id  销售统计 ID
     * @param req 销售统计入参
     * @return 影响行数
     */
    int edit(Long id, CountSaleReq req);

    /**
     * 删除销售统计
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 销售统计详情
     *
     * @param id 销售统计 ID
     * @return 销售统计视图, 无则 null
     */
    CountSaleVO detail(Long id);

    /**
     * 按查询条件取单条销售统计
     *
     * @param query 查询条件
     * @return 销售统计视图, 无则 null
     */
    CountSaleVO findByQuery(CountSaleQuery query);

    /**
     * 销售统计分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<CountSaleVO> pageList(CountSaleQuery query);
}
