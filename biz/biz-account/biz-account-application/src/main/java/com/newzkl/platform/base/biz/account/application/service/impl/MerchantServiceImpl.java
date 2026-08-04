package com.newzkl.platform.base.biz.account.application.service.impl;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.account.application.service.MerchantService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreOrderPayApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreOrderPayReq;
import com.newzkl.platform.base.biz.account.domain.adapt.api.StoreOrderPayRes;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.domain.repository.AccountRepository;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantCmd;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * {@code MerchantService} 实现
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final AccountRepository accountRepository;
    private final ChannelClientDomain channelClientDomain;
    private final StoreOrderPayApi storeOrderPayApi;

    @Override
    public StoreOrderPayRes orderPay(MerchantCmd.OrderPay orderPay, Long accountId) {
        AccountVO accountVO = accountRepository.account(null, accountId);
        if (accountVO == null) {
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "账号不存在");
        }
        boolean isChannel = StrUtil.contains(accountVO.getRoleIdList(), RoleEnum.CompanyRole.CHANNEL.getCodeStr());
        boolean isMember = StrUtil.contains(accountVO.getRoleIdList(), RoleEnum.CompanyRole.MEMBER.getCodeStr());
        if (isChannel) {
            ChannelVO channelVO = channelClientDomain.channel(accountId);
            if (channelVO == null) {
                isChannel = false;
            } else if (CommonEnum.YesOrNo.YES == channelVO.getStorePermission()) {
                throw new PlatformException(BaseErrorCode.EXIST_DATA, "门店已开通");
            }
        }
        if (!isChannel && !isMember) {
            throw new PlatformException(BaseErrorCode.OPERATE_FAIL, "未知的身份");
        }
        StoreOrderPayReq req = new StoreOrderPayReq();
        req.setPayType(orderPay.getPayType());
        req.setAccountId(accountId);
        req.setStoreInfo(TransferUtils.transfer(orderPay.getStoreInfo(), StoreOrderPayReq.StoreOrderPayInfo::new));
        return storeOrderPayApi.orderPay(req);
    }
}
