package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.domain.repository.SelectorRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.SelectorDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.SelectorDO;
import com.newzkl.platform.base.biz.account.model.req.SelectorQuery;
import com.newzkl.platform.base.biz.account.model.res.SelectorOutRes;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
// TODO[cross-domain relation]: import relation.vo.TeamUserCountRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 甄选师
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class SelectorRepositoryImpl implements SelectorRepository {

    private final SelectorDAO selectorDAO;

    @Override
    public List<SelectorOutRes> selectorListVO(List<Long> list) {
        QueryWrapper<SelectorOutRes> queryWrapper = new QueryWrapper<>();
        if (list != null) {
            if (CollUtil.isEmpty(list)) {
                return Collections.emptyList();
            } else {
                queryWrapper.in("id", list);
            }
        }

        List<SelectorOutRes> voList = selectorDAO.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(voList)) {
            return voList;
        }
        return Collections.emptyList();
    }

    @Override
    public Long selectorSave(SelectorVO selector) {
        SelectorDO selectorDO = TransferUtils.transfer(selector, SelectorDO.class);
        selectorDAO.insert(selectorDO);
        return selectorDO.getId();
    }

    @Override
    public int selectorEdit(SelectorVO selector) {
        SelectorDO selectorDO = TransferUtils.transfer(selector, SelectorDO.class);
        return selectorDAO.updateById(selectorDO);
    }

    @Override
    public int selectorDelete(List<Long> selectorIdList) {
        SelectorQuery query = new SelectorQuery();
        query.setIdList(selectorIdList);
        return selectorDAO.deleteByIds(selectorIdList);
    }

    @Override
    public void selectorEdit(List<EditColumnVO> columnList, Long id) {
        SelectorQuery query = new SelectorQuery();
        query.setId(id);
//        selectorDAO.columnByQuery(columnList, query);
    }

    @Override
    public SelectorVO selector(Long selectorId) {
        SelectorDO selectorDO = selectorDAO.selectById(selectorId);
        return TransferUtils.transfer(selectorDO, SelectorVO.class);
    }

    // TODO[cross-domain relation]: countLevelNumber(Long) 返回 relation.TeamUserCountRes, 迁 biz-user 后恢复
}
