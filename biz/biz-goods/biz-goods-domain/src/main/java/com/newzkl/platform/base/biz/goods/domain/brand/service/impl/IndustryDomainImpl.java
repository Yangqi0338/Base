package com.newzkl.platform.base.biz.goods.domain.brand.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.brand.repository.IIndustryRepository;
import com.newzkl.platform.base.biz.goods.domain.brand.service.IIndustryDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.IndustryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.IndustryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.IndustryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 品牌
 *
 * @author fang
 */
@Service
@RequiredArgsConstructor
public class IndustryDomainImpl implements IIndustryDomain {

    private final IIndustryRepository industryRepository;

    @Override
    public Long industrySave(IndustryReq industryReq) {
        if (industryReq.getId() == null) {
            industryRepository.industrySave(industryReq);
        } else {
            industryRepository.industryEdit(industryReq);
        }
        return industryReq.getId();
    }

    @Override
    public IndustryVO industry(Long id) {
        return industryRepository.industry(id);
    }

    @Override
    public void industryDelete(List<Long> idList) {
        industryRepository.industryDelete(idList);
    }

    @Override
    public Page<IndustryVO> industryPage(IndustryPageQuery query) {
        if (ObjectUtil.equals(query.getOperatorFilter(), 1)) {
            industryRepository.buildPageQuery(query);
        }
        return industryRepository.queryPage(query);
    }
}
