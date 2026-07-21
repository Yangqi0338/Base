package com.newzkl.platform.base.biz.account.domain.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelError;
import com.newzkl.platform.base.biz.account.model.req.EmpCreateReq;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;

import java.io.InputStream;
import java.util.List;

public interface AdminClientDomain {

    void batchEmpCreate(List<EmpCreateReq> empCreateReqList, Long parentAccountId);

    Page<EmpRes> empPage(EmpQuery empQuery);

    EasyExcelError excelCreateEmp(InputStream inputStream, Long accountId);
}
