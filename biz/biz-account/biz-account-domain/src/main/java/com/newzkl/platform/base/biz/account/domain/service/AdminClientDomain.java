package com.newzkl.platform.base.biz.account.domain.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.biz.account.model.req.EmpCreateReq;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;

import java.io.InputStream;
import java.util.List;

/**
 * 平台端 (员工) 领域服务
 *
 * @author fang
 */
public interface AdminClientDomain {

    /**
     * 批量新增员工
     *
     * <p>旧 {@code emp} 表自带 username/password/account_id, 一行即一员工;
     * Base 拆成 {@code account} (凭证 + 主子关系) 与 {@code emp} (类型 + 岗位) 两行同主键,
     * 故一次新增写两表</p>
     *
     * @param empCreateReqList 员工新增请求列表
     * @param parentAccountId  主账号 ID
     */
    void batchEmpCreate(List<EmpCreateReq> empCreateReqList, Long parentAccountId);

    /**
     * 员工修改
     *
     * <p>迁自旧 {@code EmpDomain.empEdit(EmpCreateReq)}: 只改传入的非空项。
     * {@code username} / {@code password} / {@code companyRoleId} 落 {@code account} 表,
     * {@code roleId} (岗位) 落 {@code emp} 表</p>
     *
     * @param empCreateReq 员工修改请求, 必带 id
     * @return 影响行数 (account 与 emp 两表之和)
     */
    int empEdit(EmpCreateReq empCreateReq);

    /**
     * 员工删除
     *
     * <p>迁自旧 {@code EmpDomain.empDelete(List)}: 旧只删 {@code emp} 单表;
     * Base 下账号凭证在 {@code account}, 只删 {@code emp} 会留下可登录的孤儿账号,
     * 故两表同删</p>
     *
     * @param empIdList 员工 (账号) ID 列表
     * @return 影响行数
     */
    int empDelete(List<Long> empIdList);

    /**
     * 员工分页
     *
     * @param empQuery 员工查询
     * @return 员工分页
     */
    Page<EmpRes> empPage(EmpQuery empQuery);

    /**
     * Excel 批量新增员工
     *
     * @param inputStream Excel 流
     * @param accountId   主账号 ID
     * @return 导入错误信息
     */
    EasyExcelErrorVO excelCreateEmp(InputStream inputStream, Long accountId);
}
