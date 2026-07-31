package com.newzkl.platform.base.biz.goods.domain.report.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuSimpleVO;

import java.util.List;

/**
 * 报告存储接口
 *
 * <p>每方法单一持久化动作 (一次 IO), 读组装/查后写由 {@code ReportDomain} 编排</p>
 *
 * @author kc
 */
public interface ReportRepository {

    /**
     * 按 ID 查报告主记录, 不含关联 SPU 列表
     *
     * @param id 报告 ID
     * @return 报告结果对象, 不存在返回 null
     */
    ReportRes report(Long id);

    /**
     * 按 SPU ID 列表查 SPU 简要视图
     *
     * @param spuIdList SPU ID 列表
     * @return SPU 简要视图列表
     */
    List<SpuSimpleVO> spuSimpleList(List<Long> spuIdList);

    /**
     * 按查询条件统计报告数
     *
     * @param query 查询条件
     * @return 命中报告数
     */
    int countByQuery(ReportQuery query);

    /**
     * 按查询条件更新报告
     *
     * @param req   报告值对象
     * @param query 查询条件
     * @return 影响行数
     */
    int updateByQuery(ReportVO req, ReportQuery query);

    /**
     * 插入报告, 回填并返回主键
     *
     * @param req 报告值对象
     * @return 报告 ID
     */
    Long insert(ReportVO req);

    /**
     * 按 ID 删除报告
     *
     * @param id 报告 ID
     */
    void del(Long id);

    /**
     * 按条件查报告列表
     *
     * @param query 查询条件
     * @return 报告列表
     */
    List<ReportVO> queryList(ReportQuery query);

    /**
     * 按条件分页查报告
     *
     * @param query 查询条件
     * @return 报告分页
     */
    Page<ReportVO> queryPage(ReportQuery query);
}
