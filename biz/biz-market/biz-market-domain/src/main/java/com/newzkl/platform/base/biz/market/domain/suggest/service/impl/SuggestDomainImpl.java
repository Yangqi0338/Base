package com.newzkl.platform.base.biz.market.domain.suggest.service.impl;

import com.newzkl.platform.base.biz.market.domain.suggest.repository.SuggestRepository;
import com.newzkl.platform.base.biz.market.domain.suggest.service.SuggestDomain;
import com.newzkl.platform.base.biz.market.model.suggest.req.CommitTagReq;
import com.newzkl.platform.base.biz.market.model.suggest.req.TagConfigReq;
import com.newzkl.platform.base.biz.market.model.suggest.vo.CommitTagVO;
import com.newzkl.platform.base.biz.market.model.suggest.vo.TagConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * {@link SuggestDomain} 实现。
 *
 * <p>迁移自 {@code com.zkl.scm.market.domain.other.service.impl.SuggestServiceImpl},
 * 与旧实现一致仅做委派。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class SuggestDomainImpl implements SuggestDomain {

    private final SuggestRepository suggestRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTagConfig(TagConfigReq req) {
        suggestRepository.saveTagConfig(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commitTag(CommitTagReq req) {
        suggestRepository.commitTag(req);
    }

    @Override
    public TagConfigVO queryTagConfig(boolean operator) {
        return suggestRepository.queryTagConfig(operator);
    }

    @Override
    public CommitTagVO queryCommitTag() {
        return suggestRepository.queryCommitTag();
    }

    @Override
    public List<CommitTagVO> queryOperatorAllSuggest(String mobile) {
        return suggestRepository.queryOperatorAllSuggest(mobile);
    }
}
