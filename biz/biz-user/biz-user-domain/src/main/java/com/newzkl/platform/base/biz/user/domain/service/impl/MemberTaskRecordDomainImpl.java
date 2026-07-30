package com.newzkl.platform.base.biz.user.domain.service.impl;

import com.newzkl.platform.base.biz.user.domain.adapt.repository.MemberTaskRecordRepository;
import com.newzkl.platform.base.biz.user.domain.service.MemberTaskRecordDomain;
import com.newzkl.platform.base.biz.user.model.task.record.query.MemberTaskRecordQuery;
import com.newzkl.platform.base.biz.user.model.task.record.res.MemberTaskRecordRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员任务进度领域服务实现
 *
 * <p>迁移自旧 {@code MemberTaskRecordDomainServiceImpl}（读侧部分）。</p>
 *
 * @author KC
 */
@Slf4j
@Service("memberTaskRecordDomainImpl")
@RequiredArgsConstructor
public class MemberTaskRecordDomainImpl implements MemberTaskRecordDomain {

    private final MemberTaskRecordRepository memberTaskRecordRepository;

    @Override
    public List<MemberTaskRecordRes> pageQuery(MemberTaskRecordQuery query) {
        // TODO[page-meta] 仓储降级为 List，total/pages 元数据跨层丢失。
        return memberTaskRecordRepository.pageQuery(query);
    }

    @Override
    public List<MemberTaskRecordRes> listAll(MemberTaskRecordQuery query) {
        return memberTaskRecordRepository.listAll(query);
    }
}
