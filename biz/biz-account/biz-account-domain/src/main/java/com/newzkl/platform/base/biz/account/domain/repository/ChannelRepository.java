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

    Boolean save(ChannelVO channel);

    int channelEdit(ChannelVO channel);

    int channelDelete(List<Long> channelIdList);

    void channelEdit(List<EditColumnVO> columnList, ChannelQuery channelQuery);

    ChannelVO channel(Long channelId);

    Page<ChannelVO> pageList(ChannelQuery channelQuery);

    /**
     * 按主键批量查渠道商身份行
     *
     * @param idList 渠道商 (账号) ID 列表
     * @return 渠道商视图列表, 无则空列表
     */
    List<ChannelVO> listByIdList(List<Long> idList);

}
