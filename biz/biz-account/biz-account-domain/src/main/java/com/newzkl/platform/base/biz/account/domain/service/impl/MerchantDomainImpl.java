package com.newzkl.platform.base.biz.account.domain.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.repository.CdkRepository;
import com.newzkl.platform.base.biz.account.domain.repository.MerchantRepository;
import com.newzkl.platform.base.biz.account.domain.service.MerchantDomain;
import com.newzkl.platform.base.biz.account.model.assembler.MerchantAssembler;
import com.newzkl.platform.base.biz.account.model.cdk.vo.CdkVO;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantQuery;
import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantReq;
import com.newzkl.platform.base.biz.account.model.merchant.res.MerchantRes;
import com.newzkl.platform.base.biz.account.model.merchant.vo.MerchantVO;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商户领域服务实现。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.service.IMerchantDomainImpl}。
 * 旧 {@code ThrowsException.exception(...)} 换为 {@link PlatformException};
 * 旧 {@code CommonEnum.Switch.ON} 在中台通用层为 {@code CommonEnum.YesOrNo.YES} (码值 1 一致);
 * 登录态取值 (accountId) 由旧的 controller/应用层下沉到本层。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class MerchantDomainImpl implements MerchantDomain {

    private final MerchantRepository merchantRepository;
    private final CdkRepository cdkRepository;
    private final MerchantAssembler assembler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(Long id, MerchantReq req) {
        MerchantVO item = assembler.req2VO(req);
        item.setId(id);
        return merchantRepository.edit(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<Long> idList) {
        return merchantRepository.delete(idList);
    }

    @Override
    public MerchantRes detail(Long id) {
        return assembler.vo2Res(merchantRepository.detail(id));
    }

    @Override
    public MerchantRes currentMerchant() {
        return detail(SecurityUtils.getAccountId());
    }

    @Override
    public Page<MerchantRes> pageList(MerchantQuery query) {
        return TransferUtils.transferPage(merchantRepository.pageList(query), assembler::vo2Res);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void customSave(MerchantCustomSaveReq req) {
        MerchantVO item = TransferUtils.transfer(req, MerchantVO::new, (c, v) -> {
            v.setId(c.getAccountId());
            v.setStorePermission(CommonEnum.YesOrNo.NO.getCode());
        });
        if (StrUtil.isEmpty(item.getName())) {
            item.setName(item.getUsername());
        }
        merchantRepository.save(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void useStoreCdk(String cdkValue) {
        Long accountId = SecurityUtils.getAccountId();
        // 开通码校验与占用
        CdkVO cdk = cdkRepository.detail(cdkRepository.idByValue(cdkValue));
        if (cdk == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "开通码");
        }
        if (CommonEnum.YesOrNo.YES.getCode().equals(cdk.getUseState())) {
            throw new PlatformException(BaseErrorCode.PARAM, "验证码已使用");
        }
        cdk.setUseState(CommonEnum.YesOrNo.YES.getCode());
        cdk.setUseId(accountId);
        cdk.setUseTime(LocalDateTime.now());
        cdkRepository.edit(cdk);

        // 开通数字门店权限
        MerchantVO merchantEdit = new MerchantVO();
        merchantEdit.setId(accountId);
        merchantEdit.setStorePermission(CommonEnum.YesOrNo.YES.getCode());
        merchantRepository.edit(merchantEdit);
    }

    @Override
    public WxMpConfigVO wxMpConfig(Long merchantId) {
        return merchantRepository.wxMpConfig(merchantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wxMpConfigSet(WxMpConfigVO wxMpConfigVO) {
        MerchantVO merchantEdit = new MerchantVO();
        merchantEdit.setId(SecurityUtils.getAccountId());
        merchantEdit.setWxMpConfig(wxMpConfigVO);
        merchantRepository.edit(merchantEdit);
    }
}
