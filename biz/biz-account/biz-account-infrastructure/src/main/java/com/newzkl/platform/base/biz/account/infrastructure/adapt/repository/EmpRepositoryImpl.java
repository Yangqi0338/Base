package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.EmpRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.EmpDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.EmpDO;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmpRepositoryImpl implements EmpRepository {

    private final EmpDAO empDAO;

    @Override
    public Page<EmpVO> pageList(EmpQuery query) {
        return TransferUtils.transferPage(empDAO.selectPage(RepositorySupport.page(query), empDAO.getLw(query)), EmpVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(EmpVO emp) {
        // emp 主键与 account 主键同值, 由领域层生成后透传, 不能走 preInsert 清 id
        return empDAO.insert(TransferUtils.transfer(emp, EmpDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(EmpVO emp) {
        return empDAO.updateById(TransferUtils.transfer(emp, EmpDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return 0;
        }
        return empDAO.deleteByIds(idList);
    }

    // OPTIMIZE 创建emp子账号,需要将邀请人也一并复制
}
