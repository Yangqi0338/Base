package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.MemberTaskRecordRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.MemberTaskRecordDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.MemberTaskRecordDO;
import com.newzkl.platform.base.biz.user.model.task.record.query.MemberTaskRecordQuery;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会员任务进度仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.repository.MemberTaskRecordRepositoryImpl}。
 * 旧实现继承 {@code ServiceImpl} 且用 MapStruct Assembler 转换，中台改为直接注入 DAO +
 * {@code TransferUtils}；旧手工 {@code eq(isDelete, 0)} 过滤由 {@code @TableLogic} 自动接管。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class MemberTaskRecordRepositoryImpl implements MemberTaskRecordRepository {

    private final MemberTaskRecordDAO memberTaskRecordDAO;

    @Override
    public List<MemberTaskRecordRes> pageQuery(MemberTaskRecordQuery query) {
        // TODO[page-meta] 领域层降级为 List，total/pages 元数据跨层丢失。
        Page<MemberTaskRecordDO> doPage = memberTaskRecordDAO.selectPage(
                RepositorySupport.page(query), memberTaskRecordDAO.getLw(query));
        return TransferUtils.transfers(doPage.getRecords(), MemberTaskRecordRes::new);
    }

    @Override
    public List<MemberTaskRecordRes> listAll(MemberTaskRecordQuery query) {
        return TransferUtils.transfers(
                memberTaskRecordDAO.selectList(memberTaskRecordDAO.getLw(query)),
                MemberTaskRecordRes::new);
    }
}
