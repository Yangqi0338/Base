package com.newzkl.platform.base.biz.account.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.EditColumnDTO;
import com.newzkl.platform.base.biz.account.model.req.AccountRegisterRes;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;

import java.util.List;

/**
 * 渠道商
 *
 * @author fang
 */
public interface ChannelRepository {

    Long channelSave(ChannelVO channel);

    int channelEdit(ChannelVO channel);

    int channelDelete(List<Long> channelIdList);

    void channelEdit(List<EditColumnDTO> columnList, ChannelQuery channelQuery);

    ChannelVO channel(Long channelId);

    Page<ChannelVO> pageList(ChannelQuery channelQuery);

    void registerEvent(AccountRegisterRes account, String password);
}
