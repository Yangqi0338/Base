package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.account.domain.repository.ChannelRepository;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.biz.account.model.assembler.identity.ChannelAssembler;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 订单
 * @date 2024/1/3115:56
 */
@Service
@RequiredArgsConstructor
public class ChannelClientDomainImpl extends IdentityAccountSupport implements ChannelClientDomain {

    private final ChannelRepository channelRepository;
    private final ChannelAssembler channelAssembler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long channelCustomSave(ChannelCustomSaveReq customSaveReq) {
        if (customSaveReq.getId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        ChannelVO item = TransferUtils.transfer(customSaveReq, ChannelVO::new);
        item.setIdentity(AccountEnum.Identity.CHANNEL);
        item.setState(ChannelEnum.State.APPLY);
        item.setAuditState(AuditEnum.State.SUCCESS);
        channelRepository.save(item);
        return item.getId();
    }

    @Override
    public Boolean channelEdit(ChannelReq channelReq) {
        return channelRepository.save(channelAssembler.req2VO(channelReq));
    }

    @Override
    public int channelDelete(List<Long> channelIdList) {
        return channelRepository.channelDelete(channelIdList);
    }

    @Override
    public void channelEdit(List<EditColumnVO> editColumnList, Long id) {
        ChannelQuery query = new ChannelQuery();
        query.setId(id);
        channelRepository.channelEdit(editColumnList, query);
    }

    @Override
    public ChannelVO channel(Long channelId) {
        ChannelVO channel = channelRepository.channel(channelId);
        return channel;
    }

    @Override
    public Boolean updateChannel(ChannelUpdateReq channelUpdateReq) {
        return channelRepository.save(channelAssembler.updateReq2VO(channelUpdateReq));
    }

    @Override
    public Page<ChannelVO> channelPageList(ChannelQuery channelQuery) {
        IdentityAccountQuery identityAccountQuery = channelAssembler.query2IdentityQuery(channelQuery);
        List<Long> accountIdList = this.findIdByQuery(AccountEnum.Client.CHANNEL, identityAccountQuery);
        CollUtil.addAll(accountIdList, channelQuery.getIdList());
        channelQuery.setIdList(accountIdList);
        return channelRepository.pageList(channelQuery);
    }
}
