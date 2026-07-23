package com.newzkl.platform.base.biz.account.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.req.ChannelCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelUpdateReq;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;

import java.util.List;

/**
 * 渠道商端服务
 *
 * @author fang
 */
public interface ChannelClientDomain {

    Long channelCustomSave(ChannelCustomSaveReq customSaveReq);

    int channelEdit(ChannelReq channelReq);

    int channelDelete(List<Long> channelIdList);

    void channelEdit(List<EditColumnVO> editColumnList, Long id);

    ChannelVO channel(Long channelId);

    /**
     * 修改渠道商
     */
    int updateChannel(ChannelUpdateReq channelCreateReq);

    Page<ChannelVO> channelPageList(ChannelQuery channelQuery);

}
