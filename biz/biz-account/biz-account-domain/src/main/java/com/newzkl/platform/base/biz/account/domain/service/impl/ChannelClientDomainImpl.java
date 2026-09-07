package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.account.domain.repository.ChannelRepository;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.dto.ChannelDTO;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.ChannelRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
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
    private final AccountDomain accountDomain;

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
    @Transactional(rollbackFor = Exception.class)
    public Boolean channelEdit(ChannelReq channelReq) {
        // username/headImg 已随建模迁到 account 表(channel 表建表脚本已 DROP 两列), 命中时先落账号侧
        if (StrUtil.isNotBlank(channelReq.getUsername()) || StrUtil.isNotBlank(channelReq.getHeadImg())) {
            // state 两侧同名不同义(渠道入驻状态 vs 账号启禁用), 放开会被 hutool 按名转枚举后抛异常, 故排除
            AccountReq accountReq = TransferUtils.transfer(channelReq, AccountReq::new,
                    (source, target) -> {
                        target.setHead(source.getHeadImg());
                        target.setIdentity(AccountEnum.Identity.CHANNEL);
                    },
                    CopyOptions.create().setIgnoreProperties("state"));
            accountDomain.accountEdit(accountReq);
        }
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
    public ChannelDTO channelBase(Long channelId) {
        return channelAssembler.vo2DTO(this.loadChannel(channelId));
    }

    @Override
    public ChannelRes channel(Long channelId) {
        ChannelRes res = channelAssembler.vo2Res(this.loadChannel(channelId));
        // 副数据: 账号侧展示字段。channelId 同时是账号ID, 账号缺失时 accountDomain 内部抛 NO_EXIST
        AccountVO account = accountDomain.account(AccountEnum.Client.CHANNEL, channelId);
        res.setUsername(account.getUsername());
        res.setRealName(account.getRealName());
        res.setNickname(account.getNickname());
        res.setHead(account.getHead());
        res.setPhone(account.getPhone());
        res.setYqm(account.getYqm());
        res.setAccountState(account.getState());
        res.setLastLoginTime(account.getLastLoginTime());
        res.setTripartiteAccountPermission(account.getTripartiteAccountPermission());
        return res;
    }

    /**
     * 取渠道商主数据行
     *
     * @param channelId 渠道商账号ID
     * @return 渠道商视图
     */
    private ChannelVO loadChannel(Long channelId) {
        ChannelVO channel = channelRepository.channel(channelId);
        if (channel == null) {
            throw new PlatformException(AccountErrorCode.NO_EXIST);
        }
        return channel;
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
