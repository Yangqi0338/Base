package com.newzkl.platform.base.biz.account.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.biz.account.model.dto.ChannelDTO;
import com.newzkl.platform.base.biz.account.model.req.ChannelCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.res.ChannelRes;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;

import java.util.List;

/**
 * 渠道商端服务
 *
 * @author fang
 */
public interface ChannelClientDomain {

    Long channelCustomSave(ChannelCustomSaveReq customSaveReq);

    /**
     * 渠道商修改
     *
     * <p>同时落两张表: channel 自有列走 channel 表, {@code username}/{@code headImg} 走 account 表
     * (建模时已从 channel 表 DROP)。仅在请求带这两个字段时才发起账号更新。</p>
     *
     * <p>{@code state} 不传播到 account —— channel 侧是入驻状态 (APPLY/IN/OPEN),
     * account 侧是启禁用 (DISABLE/ENABLE), 两者不同义</p>
     *
     * @param channelReq 渠道商修改请求
     * @return 是否修改成功
     */
    Boolean channelEdit(ChannelReq channelReq);

    int channelDelete(List<Long> channelIdList);

    void channelEdit(List<EditColumnVO> editColumnList, Long id);

    /**
     * 渠道商纯净详情
     *
     * @param channelId 渠道商账号ID
     * @return 只含 channel 自有列的纯净视图
     * @ext 主数据 channel (无副数据)。需要账号信息走 {@link #channel(Long)}
     */
    ChannelDTO channelBase(Long channelId);

    /**
     * 渠道商详情
     *
     * <p>channel 行 + account 行聚合。account 侧只取展示字段, 不含 {@code password}</p>
     *
     * @param channelId 渠道商账号ID (channel 表主键即账号ID)
     * @return 渠道商聚合视图
     * @ext 主数据 channel, 副数据 account(单副, 副数据不再向下关联)。方向与
     *      {@code AccountController.identityDetail}(主 account / 副身份) 相反, 两者不可互相替代
     */
    ChannelRes channel(Long channelId);

    Page<ChannelVO> channelPageList(ChannelQuery channelQuery);

}
