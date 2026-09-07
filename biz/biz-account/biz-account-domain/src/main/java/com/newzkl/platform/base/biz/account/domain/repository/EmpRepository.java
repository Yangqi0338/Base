package com.newzkl.platform.base.biz.account.domain.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;

import java.util.List;

/**
 * 员工身份仓储
 *
 * @author fang
 */
public interface EmpRepository {

    /**
     * 员工分页
     *
     * @param query 员工查询
     * @return 员工分页
     */
    Page<EmpVO> pageList(EmpQuery query);

    /**
     * 按主键查员工身份行
     *
     * @param empId 员工 (账号) ID
     * @return 员工视图, 无则 null
     * @ext 主数据 emp (无副数据)
     */
    EmpVO emp(Long empId);

    /**
     * 新增员工身份行
     *
     * <p>主键与 {@code account} 行共用, 由领域层生成后透传, 故此处不清 id</p>
     *
     * @param emp 员工视图
     * @return 影响行数
     */
    int save(EmpVO emp);

    /**
     * 按主键改员工身份行
     *
     * @param emp 员工视图, 只写非空列
     * @return 影响行数
     */
    int edit(EmpVO emp);

    /**
     * 按主键批量删员工身份行
     *
     * @param idList 员工 (账号) ID 列表
     * @return 影响行数
     */
    int delete(List<Long> idList);

    /**
     * 按主键批量查员工身份行
     *
     * @param idList 员工 (账号) ID 列表
     * @return 员工视图列表, 无则空列表
     */
    List<EmpVO> listByIdList(List<Long> idList);
}
