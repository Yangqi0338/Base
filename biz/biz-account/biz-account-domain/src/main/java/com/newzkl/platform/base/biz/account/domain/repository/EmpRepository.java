package com.newzkl.platform.base.biz.account.domain.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;

public interface EmpRepository {

    Page<EmpVO> pageList(EmpQuery query);
}
