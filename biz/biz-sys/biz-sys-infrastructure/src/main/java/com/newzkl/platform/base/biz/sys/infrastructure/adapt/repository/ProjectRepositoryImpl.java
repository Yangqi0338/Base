package com.newzkl.platform.base.biz.sys.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.domain.adapt.repository.ProjectRepository;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.ProjectDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.ProjectVideoDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.ProjectDO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.ProjectVideoDO;
import com.newzkl.platform.base.biz.sys.model.enums.CacheKey;
import com.newzkl.platform.base.biz.sys.model.project.query.ProjectQuery;
import com.newzkl.platform.base.biz.sys.model.project.req.ProjectSaveReq;
import com.newzkl.platform.base.biz.sys.model.project.res.ProjectListRes;
import com.newzkl.platform.base.biz.sys.model.project.res.ProjectRes;
import com.newzkl.platform.base.biz.sys.model.project.res.ProjectVideoRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 项目仓储实现
 *
 * <p>迁移说明:</p>
 * <ul>
 *   <li>源用 mapstruct {@code ProjectAssembler} 做 DO↔领域对象转换 (含 flags 的
 *       串/列表互转), 此处按 Base 硬规改 {@code TransferUtils} + 显式 flags 转换。</li>
 *   <li>意向态 (isInterest/interestNum) 仍存 Redis, 键与源 {@code CacheKey} 逐字一致。</li>
 *   <li>分页在本层内部执行 (Page 不外泄), 对领域层降级为 List。</li>
 * </ul>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    /**
     * 意向开关: 感兴趣
     */
    private static final Integer INTEREST_ON = 1;

    private final ProjectDAO projectDAO;
    private final ProjectVideoDAO projectVideoDAO;

    @Override
    public ProjectRes detail(Long accountId, Long id) {
        ProjectDO projectDO = projectDAO.selectById(id);
        if (projectDO == null) {
            return null;
        }
        ProjectRes projectRes = TransferUtils.transfer(projectDO, ProjectRes::new);
        projectRes.setFlags(str2List(projectDO.getFlags()));
        projectRes.setIsInterest(isInterest(accountId, id));
        projectRes.setVideoList(TransferUtils.transfers(
                projectVideoDAO.selectList(projectVideoDAO.getLwByProjectId(id)), ProjectVideoRes::new));
        return projectRes;
    }

    @Override
    public List<ProjectListRes> queryList(ProjectQuery query) {
        Long accountId = query.getAccountId();
        return projectDAO.selectList(projectDAO.getLw(query)).stream()
                .map(it -> do2ListRes(accountId, it))
                .toList();
    }

    @Override
    public Page<ProjectListRes> queryPage(ProjectQuery query) {
        Long accountId = query.getAccountId();
        Page<ProjectDO> page = projectDAO.selectPage(RepositorySupport.page(query), projectDAO.getLw(query));
        return TransferUtils.transferPage(page, it -> do2ListRes(accountId, it));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(ProjectSaveReq req) {
        ProjectDO projectDO = TransferUtils.transfer(req, ProjectDO::new);
        projectDO.setFlags(list2Str(req.getFlags()));
        projectDAO.insert(projectDO);

        List<ProjectVideoDO> videoList = buildVideoList(req, projectDO.getId());
        if (CollUtil.isNotEmpty(videoList)) {
            projectVideoDAO.insert(videoList);
        }
        return projectDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(ProjectSaveReq req) {
        Long id = req.getId();
        if (projectDAO.selectById(id) == null) {
            throw new PlatformException(BaseErrorCode.INVALID_UPDATE);
        }

        ProjectDO projectDO = TransferUtils.transfer(req, ProjectDO::new);
        projectDO.setFlags(list2Str(req.getFlags()));
        projectDAO.updateById(projectDO);

        List<ProjectVideoDO> videoList = buildVideoList(req, id);
        if (CollUtil.isNotEmpty(videoList)) {
            projectVideoDAO.insertOrUpdate(videoList);
        }
        List<Long> videoIdList = videoList.stream()
                .map(ProjectVideoDO::getId).filter(Objects::nonNull).toList();
        if (CollUtil.isEmpty(videoIdList)) {
            projectVideoDAO.delete(projectVideoDAO.getLwByProjectId(id));
        } else {
            projectVideoDAO.delete(projectVideoDAO.getLwByProjectId(id)
                    .notIn(ProjectVideoDO::getId, videoIdList));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        projectDAO.deleteById(id);
        projectVideoDAO.delete(projectVideoDAO.getLwByProjectId(id));
    }

    @Override
    public void interest(Long accountId, Long id, Integer interest) {
        if (accountId == null || accountId == 0) {
            return;
        }
        if (INTEREST_ON.equals(interest)) {
            RedisUtil.sAdd(StrUtil.format(CacheKey.INTEREST_PROJECT, accountId), id);
            RedisUtil.incrBy(StrUtil.format(CacheKey.PROJECT_INTEREST_NUM, id));
        } else {
            RedisUtil.sRem(StrUtil.format(CacheKey.INTEREST_PROJECT, accountId), id);
            RedisUtil.decrBy(StrUtil.format(CacheKey.PROJECT_INTEREST_NUM, id));
        }
    }

    /**
     * DO 转列表视图 (回填意向态与封面视频)
     *
     * @param accountId 当前登录账号 id
     * @param entity    项目数据对象
     * @return 项目列表视图
     */
    private ProjectListRes do2ListRes(Long accountId, ProjectDO entity) {
        ProjectListRes listRes = TransferUtils.transfer(entity, ProjectListRes::new);
        Long id = entity.getId();
        listRes.setFlags(str2List(entity.getFlags()));
        listRes.setInterestNum(interestNum(id));
        listRes.setIsInterest(isInterest(accountId, id));
        listRes.setCoverVideo(TransferUtils.transfer(
                projectVideoDAO.selectOne(projectVideoDAO.getLwByProjectId(id).last("limit 1")),
                ProjectVideoRes::new));
        return listRes;
    }

    /**
     * 按入参次序重排视频 index 并绑定项目 id
     *
     * @param req       保存请求
     * @param projectId 项目 id
     * @return 视频数据对象列表, 永不为 null
     */
    private List<ProjectVideoDO> buildVideoList(ProjectSaveReq req, Long projectId) {
        if (CollUtil.isEmpty(req.getVideoList())) {
            return List.of();
        }
        AtomicInteger index = new AtomicInteger(0);
        return req.getVideoList().stream().map(it -> {
            ProjectVideoDO videoDO = TransferUtils.transfer(it, ProjectVideoDO::new);
            videoDO.setIndex(index.getAndIncrement());
            videoDO.setProjectId(projectId);
            return videoDO;
        }).toList();
    }

    /**
     * 判断账号是否已表示意向
     *
     * @param accountId 账号 id
     * @param id        项目 id
     * @return 已表示意向返回 true
     */
    private boolean isInterest(Long accountId, Long id) {
        if (accountId == null) {
            return false;
        }
        return RedisUtil.sContains(StrUtil.format(CacheKey.INTEREST_PROJECT, accountId), id);
    }

    /**
     * 读取项目意向人数
     *
     * @param id 项目 id
     * @return 意向人数
     */
    private int interestNum(Long id) {
        return (int) RedisUtil.numGet(StrUtil.format(CacheKey.PROJECT_INTEREST_NUM, id));
    }

    /**
     * 逗号分隔串转标签列表
     *
     * @param str 逗号分隔串
     * @return 标签列表, 永不为 null
     */
    private List<String> str2List(String str) {
        return StrUtil.split(str, ',');
    }

    /**
     * 标签列表转逗号分隔串
     *
     * @param strList 标签列表
     * @return 逗号分隔串
     */
    private String list2Str(List<String> strList) {
        return CollUtil.join(strList, ",");
    }
}
