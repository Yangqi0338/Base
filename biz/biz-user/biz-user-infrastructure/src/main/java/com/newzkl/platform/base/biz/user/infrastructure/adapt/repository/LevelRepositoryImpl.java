package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.user.domain.adapt.repository.LevelRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.LevelDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.LevelDO;
import com.newzkl.platform.base.biz.user.model.relation.req.LevelQuery;
import com.newzkl.platform.base.biz.user.model.relation.res.LevelDTO;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.TeamCondition;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.TeamDirectCondition;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.TeamNoDirectCondition;
import com.newzkl.platform.base.biz.user.model.relation.vo.LevelVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 等级仓储实现。
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class LevelRepositoryImpl implements LevelRepository {

    private final LevelDAO levelDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(LevelDTO level) {
        LevelDO levelDO = TransferUtils.transfer(level, LevelDO::new);
        levelDAO.insertOrUpdate(levelDO);
        return levelDO.getId();
    }

    @Override
    public LevelDTO detail(Long levelId) {
        LevelDO levelDO = levelDAO.selectById(levelId);
        return TransferUtils.transfer(levelDO, LevelDTO::new);
    }

    @Override
    public List<LevelDTO> list(LevelQuery levelQuery) {
        List<LevelDO> levelDOList = levelDAO.selectList(levelDAO.getLw(levelQuery));
        return TransferUtils.transfers(levelDOList, LevelDTO.class);
    }

    @Override
    public List<LevelVO> calList(LevelQuery levelQuery) {
        List<LevelDO> levelDOList = levelDAO.selectList(levelDAO.getLw(levelQuery));
        List<LevelVO> list = TransferUtils.transfers(levelDOList, LevelVO.class);
        for (LevelVO levelVO : list) {
            if (levelVO.getCondition() == null) {
                continue;
            }
            levelVO.getCondition().forEach(condition -> {
                if (condition instanceof TeamCondition teamCondition) {
                    teamCondition.setRole(upRoleMerge(teamCondition.getRole()));
                }
                if (condition instanceof TeamDirectCondition teamCondition) {
                    teamCondition.setRole(upRoleMerge(teamCondition.getRole()));
                }
                if (condition instanceof TeamNoDirectCondition teamCondition) {
                    teamCondition.setRole(upRoleMerge(teamCondition.getRole()));
                }
            });
        }
        return list;
    }

    /**
     * 获取向上兼容的角色id, 方便升级。
     *
     * <p>TODO 源码依赖 ScmUtil.getOperatorLevelUpEnumList(未迁移, 属运营商身份体系)，
     * 迁移期直接返回原列表, 待身份升级链路迁移后补齐向上合并逻辑。</p>
     *
     * @param sourceRoleList 源角色id列表
     * @return 合并后的角色id列表
     */
    public List<Long> upRoleMerge(List<Long> sourceRoleList) {
        // TODO[cross-domain] ScmUtil.getOperatorLevelUpEnumList 未迁移，暂原样返回。
        return sourceRoleList;
    }
}
