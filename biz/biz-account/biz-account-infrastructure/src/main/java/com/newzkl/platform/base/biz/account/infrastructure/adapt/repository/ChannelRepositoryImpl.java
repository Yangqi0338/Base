package com.newzkl.platform.base.biz.account.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.domain.repository.ChannelRepository;
import com.newzkl.platform.base.biz.account.infrastructure.dao.ChannelDAO;
import com.newzkl.platform.base.biz.account.infrastructure.entity.ChannelDO;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 渠道商
 *
 * @author fang
 */
@Repository
@RequiredArgsConstructor
public class ChannelRepositoryImpl extends RepositorySupport implements ChannelRepository {

    private final ChannelDAO channelDAO;

    @Override
    public Boolean save(ChannelVO channel) {
        ChannelDO channelDO = TransferUtils.transfer(channel, ChannelDO.class);
        boolean res = channelDAO.insertOrUpdate(channelDO);
        channel.setId(channelDO.getId());
        return res;
    }

    @Override
    public int channelEdit(ChannelVO channel) {
        ChannelDO channelDO = TransferUtils.transfer(channel, ChannelDO.class);
        return channelDAO.updateById(channelDO);
    }

    @Override
    public int channelDelete(List<Long> channelIdList) {
        return channelDAO.deleteByIds(channelIdList);
    }

    @Override
    public void channelEdit(List<EditColumnVO> columnList, ChannelQuery query) {
        channelDAO.columnByQuery(columnList, channelDAO.getLw(query));
    }

    @Override
    public ChannelVO channel(Long channelId) {
        ChannelDO channelDO = channelDAO.selectById(channelId);
        return TransferUtils.transfer(channelDO, ChannelVO.class);
    }

    @Override
    public Page<ChannelVO> pageList(ChannelQuery query) {
        Page<ChannelDO> channelPage = channelDAO.selectPage(RepositorySupport.page(query), channelDAO.getLw(query));
        return TransferUtils.transferPage(channelPage, ChannelVO.class);
    }

}
