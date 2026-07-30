package com.newzkl.platform.base.biz.sys.domain.service.impl;

import com.newzkl.platform.base.biz.sys.domain.adapt.repository.TemplateConfigRepository;
import com.newzkl.platform.base.biz.sys.domain.service.TemplateConfigDomain;
import com.newzkl.platform.base.biz.sys.model.template.query.TemplateQuery;
import com.newzkl.platform.base.biz.sys.model.template.req.TemplateAddReq;
import com.newzkl.platform.base.biz.sys.model.template.req.TemplateUpdateReq;
import com.newzkl.platform.base.biz.sys.model.template.res.TemplateConfigRes;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板配置领域服务实现
 *
 * <p>迁移说明: 源在 controller 内调 {@code SecurityUtils.getAccountId()} 取创建人后
 * 传入 service, 此处按 Base 约定把当前登录态取值下沉到领域层, 不让 action 层承担。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class TemplateConfigDomainImpl implements TemplateConfigDomain {

    private final TemplateConfigRepository templateConfigRepository;

    @Override
    public Long create(TemplateAddReq req) {
        return templateConfigRepository.insert(req, SecurityUtils.getAccountId());
    }

    @Override
    public void update(TemplateUpdateReq req) {
        templateConfigRepository.update(req);
    }

    @Override
    public void delete(Long id) {
        templateConfigRepository.delete(id);
    }

    @Override
    public TemplateConfigRes detail(Long id) {
        return templateConfigRepository.detail(id);
    }

    @Override
    public List<TemplateConfigRes> page(TemplateQuery query) {
        return templateConfigRepository.page(query);
    }

    @Override
    public void setDefault(String templateId) {
        templateConfigRepository.setDefault(templateId);
    }

    @Override
    public TemplateConfigRes getDefault() {
        return templateConfigRepository.getDefault();
    }

    @Override
    public void operate(Long id, int operation) {
        templateConfigRepository.operate(id, operation);
    }
}
