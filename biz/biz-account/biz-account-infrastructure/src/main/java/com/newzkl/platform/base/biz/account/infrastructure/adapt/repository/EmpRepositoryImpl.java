package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.EmpRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.EmpDAO;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmpRepositoryImpl implements EmpRepository {

    private final EmpDAO empDAO;

    @Override
    public Page<EmpVO> pageList(EmpQuery query) {
        return TransferUtils.transferPage(empDAO.selectPage(RepositorySupport.page(query), empDAO.getLw(query)), EmpVO.class);
    }

    // OPTIMIZE 创建emp子账号,需要将邀请人也一并复制
}
