package com.newzkl.platform.base.biz.sys.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.domain.adapt.repository.ProjectRepository;
import com.newzkl.platform.base.biz.sys.domain.service.ProjectDomain;
import com.newzkl.platform.base.biz.sys.model.project.query.ProjectQuery;
import com.newzkl.platform.base.biz.sys.model.project.req.ProjectSaveReq;
import com.newzkl.platform.base.biz.sys.model.project.res.ProjectListRes;
import com.newzkl.platform.base.biz.sys.model.project.res.ProjectRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目领域服务实现
 *
 * <p>迁移说明: 源实现在 domain 层直接调 {@code SecurityUtils.getAccountId()} 取当前登录人,
 * 此处保持同一取值位置, 避免把当前登录态渗进 model。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class ProjectDomainImpl implements ProjectDomain {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectRes detail(Long id) {
        ProjectRes projectRes = projectRepository.detail(SecurityUtils.getAccountId(), id);
        ThrowsException.isNull(projectRes, BaseErrorCode.NODATA, "项目");
        return projectRes;
    }

    @Override
    public List<ProjectListRes> queryList(ProjectQuery query) {
        return projectRepository.queryList(query);
    }

    @Override
    public Page<ProjectListRes> queryPage(ProjectQuery query) {
        return projectRepository.queryPage(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ProjectSaveReq req) {
        Long projectId = projectRepository.insertProject(req);
        projectRepository.insertProjectVideos(req, projectId);
        return projectId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(ProjectSaveReq req) {
        Long id = req.getId();
        if (!projectRepository.existsProject(id)) {
            throw new PlatformException(BaseErrorCode.INVALID_UPDATE);
        }
        projectRepository.updateProject(req);
        List<Long> keepVideoIds = projectRepository.saveProjectVideos(req, id);
        projectRepository.deleteStaleVideos(id, keepVideoIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        projectRepository.deleteProject(id);
        projectRepository.deleteProjectVideos(id);
    }

    @Override
    public void interest(Long id, Integer interest) {
        projectRepository.interest(SecurityUtils.getAccountId(), id, interest);
    }
}
