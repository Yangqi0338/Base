package com.newzkl.platform.base.biz.account.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
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

    void channelEdit(List<EditColumnVO> columnList, ChannelQuery channelQuery);

    ChannelVO channel(Long channelId);

    Page<ChannelVO> pageList(ChannelQuery channelQuery);

}
