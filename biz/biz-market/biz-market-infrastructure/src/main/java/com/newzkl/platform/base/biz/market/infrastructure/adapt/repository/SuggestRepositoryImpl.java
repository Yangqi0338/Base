package com.newzkl.platform.base.biz.market.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.market.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.market.domain.adapt.api.UpIdRes;
import com.newzkl.platform.base.biz.market.domain.suggest.repository.SuggestRepository;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketSuggestTagConfigDAO;
import com.newzkl.platform.base.biz.market.infrastructure.dao.MarketSuggestTagDAO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketSuggestTagConfigDO;
import com.newzkl.platform.base.biz.market.infrastructure.entity.MarketSuggestTagDO;
import com.newzkl.platform.base.biz.market.model.suggest.query.SuggestTagQuery;
import com.newzkl.platform.base.biz.market.model.suggest.req.CommitTagReq;
import com.newzkl.platform.base.biz.market.model.suggest.req.TagConfigReq;
import com.newzkl.platform.base.biz.market.model.suggest.vo.CommitTagVO;
import com.newzkl.platform.base.biz.market.model.suggest.vo.TagConfigVO;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code SuggestRepository} 实现
 *
 * <p>迁移自 {@code com.zkl.scm.market.infrastructure.repository.SuggestRepositoryImpl};
 * 原 {@code @DubboReference IAccountFacade} 直连改为 {@code AccountApi} 出站端口,
 * 原手写 XML 改为 MyBatis-Plus 条件组装, 原 {@code JsonUtils} 改为 hutool {@code JSONUtil}。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class SuggestRepositoryImpl implements SuggestRepository {

    private final MarketSuggestTagDAO marketSuggestTagDAO;

    private final MarketSuggestTagConfigDAO marketSuggestTagConfigDAO;

    private final AccountApi accountApi;

    @Override
    public void saveTagConfig(TagConfigReq req) {
        MarketSuggestTagConfigDO configDO = new MarketSuggestTagConfigDO();
        configDO.setTagConfigSelect(req.getTagConfigSelect());
        // 迁移保留旧行为: nowTagSelect 取自 req.tagConfigSelect (非 req.nowTagSelect),
        // 疑为旧代码笔误, 但前端已按此行为对齐, 不做"优化"。
        configDO.setNowTagSelect(req.getTagConfigSelect());
        configDO.setMinNum(req.getMinNum());
        configDO.setId(req.getId());
        configDO.setTagConfig(toJson(req.getTagConfig()));
        configDO.setNowTag(toJson(req.getNowTag()));
        if (configDO.getId() == null) {
            configDO.setAccountId(SecurityUtils.getAccountId());
            marketSuggestTagConfigDAO.insert(configDO);
        } else {
            marketSuggestTagConfigDAO.updateById(configDO);
        }
    }

    @Override
    public void commitTag(CommitTagReq req) {
        UpIdRes upIdRes = accountApi.upId(SecurityUtils.getAccountId());
        MarketSuggestTagDO tagDO = new MarketSuggestTagDO();
        tagDO.setTagConfig(toJson(req.getTagConfig()));
        tagDO.setNowConfig(toJson(req.getNowTag()));
        tagDO.setAccountId(SecurityUtils.getAccountId());
        tagDO.setRemark(req.getRemark());
        tagDO.setOpeartorId(upIdRes == null ? null : upIdRes.getOneId());
        tagDO.setMobile(SecurityUtils.getUsername());
        marketSuggestTagDAO.insert(tagDO);
    }

    @Override
    public TagConfigVO queryTagConfig(boolean operator) {
        Long accountId = SecurityUtils.getAccountId();
        if (!operator) {
            UpIdRes upIdRes = accountApi.upId(accountId);
            accountId = upIdRes == null ? null : upIdRes.getOneId();
        }
        MarketSuggestTagConfigDO configDO =
                marketSuggestTagConfigDAO.selectOne(marketSuggestTagConfigDAO.getLwByAccount(accountId));
        if (configDO == null) {
            return null;
        }
        TagConfigVO vo = new TagConfigVO();
        vo.setId(configDO.getId());
        vo.setTagConfigSelect(configDO.getTagConfigSelect());
        vo.setNowTagSelect(configDO.getNowTagSelect());
        vo.setMinNum(configDO.getMinNum());
        vo.setTagConfig(toStringList(configDO.getTagConfig()));
        vo.setNowTag(toStringList(configDO.getNowTag()));
        return vo;
    }

    @Override
    public CommitTagVO queryCommitTag() {
        SuggestTagQuery query = new SuggestTagQuery();
        query.setAccountId(SecurityUtils.getAccountId());
        query.setOperator(false);
        List<MarketSuggestTagDO> list = marketSuggestTagDAO.selectList(marketSuggestTagDAO.getLw(query));
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        // 迁移保留旧行为: 结果按 create_time DESC 排序后取"最后一条", 即最早的一次提交。
        // 语义上更像应取 get(0), 但前端已按此行为对齐, 不做"优化"。
        return toCommitTagVO(list.get(list.size() - 1));
    }

    @Override
    public List<CommitTagVO> queryOperatorAllSuggest(String mobile) {
        SuggestTagQuery query = new SuggestTagQuery();
        query.setAccountId(SecurityUtils.getAccountId());
        query.setOperator(true);
        query.setMobile(mobile);
        List<MarketSuggestTagDO> list = marketSuggestTagDAO.selectList(marketSuggestTagDAO.getLw(query));
        List<CommitTagVO> result = new ArrayList<>();
        if (CollUtil.isEmpty(list)) {
            // 迁移变更: 旧实现返回 null, 新实现按"集合恒非空"规范返回空集合。
            return result;
        }
        for (MarketSuggestTagDO tagDO : list) {
            result.add(toCommitTagVO(tagDO));
        }
        return result;
    }

    /**
     * DO 转提交视图
     *
     * @param tagDO 提交记录
     * @return 提交视图
     */
    private CommitTagVO toCommitTagVO(MarketSuggestTagDO tagDO) {
        CommitTagVO vo = new CommitTagVO();
        vo.setId(tagDO.getId());
        vo.setTagConfig(toStringList(tagDO.getTagConfig()));
        vo.setNowTag(toStringList(tagDO.getNowConfig()));
        vo.setRemark(tagDO.getRemark());
        vo.setState(tagDO.getState());
        vo.setMobile(tagDO.getMobile());
        return vo;
    }

    /**
     * 集合转 JSON 字符串
     *
     * @param list 字符串集合
     * @return JSON 字符串, 入参为空时返回 null
     */
    private String toJson(List<String> list) {
        if (list == null) {
            return null;
        }
        return JSONUtil.toJsonStr(list);
    }

    /**
     * JSON 字符串转字符串集合
     *
     * @param json JSON 字符串
     * @return 字符串集合, 恒非 null
     */
    private List<String> toStringList(String json) {
        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }
        return JSONUtil.parseArray(json).toList(String.class);
    }
}
