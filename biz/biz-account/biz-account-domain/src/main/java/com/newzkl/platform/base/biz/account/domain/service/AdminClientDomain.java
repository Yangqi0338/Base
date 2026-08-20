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
}
