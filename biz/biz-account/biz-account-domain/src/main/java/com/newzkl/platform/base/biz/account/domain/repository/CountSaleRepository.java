package com.newzkl.platform.base.biz.account.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.CountSaleQuery;
import com.newzkl.platform.base.biz.account.model.res.CountSaleVO;

import java.util.List;

/**
 * 销售统计仓储端口。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.count.repository.ICountSaleRepository}。
 * 旧接口的 {@code countSaleEdit(List<EditColumnDTO>, Long)} (列自增) 与
 * {@code resetUserOrderCount()} (清空统计表并联动重置 supplier / channel 冗余列) 属定时任务能力,
 * 无本轮端点调用, 未随本切片迁移。</p>
 *
 * @author KC
 */
public interface CountSaleRepository {

    /**
     * 保存销售统计。
     *
     * @param countSale 销售统计领域视图 (需带 id)
     * @return 主键 ID
     */
    Long save(CountSaleVO countSale);

    /**
     * 按主键更新销售统计 (仅更新非 null 列)。
     *
     * @param countSale 销售统计领域视图 (需带 id)
     * @return 影响行数
     */
    int edit(CountSaleVO countSale);

    /**
     * 按 ID 列表删除销售统计。
     *
     * @param idList ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 销售统计详情。
     *
     * @param id 主键 ID
     * @return 销售统计领域视图, 无则 null
     */
    CountSaleVO detail(Long id);

    /**
     * 按查询条件取单条销售统计。
     *
     * @param query 查询条件
     * @return 销售统计领域视图, 无则 null
     */
    CountSaleVO findByQuery(CountSaleQuery query);

    /**
     * 按查询条件取销售统计列表。
     *
     * @param query 查询条件
     * @return 列表, 无数据返回空集合
     */
    List<CountSaleVO> list(CountSaleQuery query);

    /**
     * 销售统计分页。
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<CountSaleVO> pageList(CountSaleQuery query);
}
