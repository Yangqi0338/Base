package com.newzkl.platform.base.biz.finance.application.purse.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.purse.service.TripartitePurseService;
import com.newzkl.platform.base.biz.finance.domain.hf.HuiFuMethod;
import com.newzkl.platform.base.biz.finance.domain.purse.service.TripartitePurseDomain;
import com.newzkl.platform.base.biz.finance.model.pay.vo.CommitInfoExt;
import com.newzkl.platform.base.biz.finance.model.purse.req.EntUserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.UserApplyAccountReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.huifu.*;
import com.newzkl.platform.base.biz.finance.model.purse.res.EntUserApplyAccountRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.UserApplyAccountRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.huifu.AccountBindSyncRes;
import com.newzkl.platform.base.biz.finance.model.purse.res.huifu.OpenAccountRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountTripartitePurseVO;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 汇付三方钱包处理编排实现
 *
 * @author kc
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HuiFuTripartitePurseServiceImpl implements TripartitePurseService {

    private final TripartitePurseDomain tripartitePurseDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntUserApplyAccountRes addEntAccountTripartitePurse(EntUserApplyAccountReq command) {
        if (!(command instanceof HuiFuEntUserApplyAccountReq saveCommand)) {
            throw new PlatformException(BaseErrorCode.NOT_SERVICE);
        }

        String huifuId = saveCommand.getHuifuId();
        // 若是新增，判断账号是否已经发起过绑定三方账号
        boolean doUpdateAction = this.checkSave(huifuId);
        Long accountId = SecurityUtils.getAccountId();

        // 转化为汇付的开户请求
        HuiFuEntUserOpenAccountReq accountReq = saveCommand.getAccountReq();
        accountReq.setHuifuId(huifuId);

        // 开户或修改开户信息
        OpenAccountRes openAccountRes = doUpdateAction ? HuiFuMethod.entModifyAccount(accountReq) : HuiFuMethod.entOpenAccount(accountReq);
        if (openAccountRes.isSuccess()) {
            throw new PlatformException(openAccountRes.getReqCode(), openAccountRes.getReqDesc());
        }

        // 开户成功才能绑卡
        huifuId = openAccountRes.getHuifuId();
        HuiFuBindCardReq cardReq = saveCommand.getCardReq();
        cardReq.setHuifuId(huifuId);

        // 绑卡或修改绑卡信息
        AccountBindSyncRes syncRes = doUpdateAction ? HuiFuMethod.userModifyCard(cardReq) : HuiFuMethod.userBindCard(cardReq);

        if (!syncRes.isSuccess()) {
            throw new PlatformException(BaseErrorCode.PARAM_JSON.getCode(), syncRes.getReqDesc());
        }

        String tokenNo = syncRes.getTokenNo();
        String applyNo = syncRes.getApplyNo();
        // 这里是汇付客户id,非平台id
        huifuId = syncRes.getHuifuId();

        // 将两次请求返回的信息封装到commitInfo
        CommitInfoExt commitInfoExt = TransferUtils.transfer(saveCommand.getInfoExt(), CommitInfoExt.class);

        commitInfoExt.setBindCardCashSeqId(tokenNo);
        commitInfoExt.setHuifuId(huifuId);

        saveCommand.setInfoExt(commitInfoExt);

        // 新增三方账号信息
        AccountTripartitePurseVO tripartitePurseVO = this.buildCommonTripartitePurse(huifuId, accountId, cardReq.getCardNo(), syncRes.isSuccess());
        tripartitePurseVO.setAccountName(accountReq.getEntName());
        tripartitePurseVO.setCommitInfo(JSONUtil.toJsonStr(saveCommand));
        tripartitePurseVO.setOidApplySeqNo(applyNo);

        tripartitePurseDomain.addAccountTripartitePurse(tripartitePurseVO);

        // TODO
        EntUserApplyAccountRes entUserApplyAccountResult = new EntUserApplyAccountRes();

        return entUserApplyAccountResult;
    }

    @Override
    public UserApplyAccountRes addAccountTripartitePurse(UserApplyAccountReq command) {
        if (!(command instanceof HuiFuUserApplyAccountReq saveCommand)) {
            throw new PlatformException(BaseErrorCode.NOT_SERVICE);
        }

        String huifuId = saveCommand.getHuifuId();
        // 若是新增，判断账号是否已经发起过绑定三方账号
        boolean doUpdateAction = this.checkSave(huifuId);
        Long accountId = SecurityUtils.getAccountId();

        String openAccountReqSeqId = accountId + "OA" + SnowflakeIdAble.getSnowflakeId();
        String bindCardReqSeqId = accountId + "BC" + SnowflakeIdAble.getSnowflakeId();

        // 转化为汇付的开户请求
        HuiFuUserOpenAccountReq accountReq = saveCommand.getAccountReq();
        accountReq.setHuifuId(huifuId);

        // 开户或修改开户信息
        OpenAccountRes openAccountRes = doUpdateAction ? HuiFuMethod.userModifyAccount(accountReq) : HuiFuMethod.userOpenAccount(accountReq);
        if (!openAccountRes.isSuccess()) {
            throw new PlatformException(BaseErrorCode.PARAM_JSON.getCode(), openAccountRes.getReqDesc());
        }

        huifuId = openAccountRes.getHuifuId();
        HuiFuBindCardReq cardReq = saveCommand.getCardReq();
        cardReq.setHuifuId(huifuId);

        // 绑卡或修改绑卡请求
        AccountBindSyncRes syncRes = doUpdateAction ? HuiFuMethod.userModifyCard(cardReq) : HuiFuMethod.userBindCard(cardReq);

        if (!syncRes.isSuccess()) {
            throw new PlatformException(BaseErrorCode.PARAM_JSON.getCode(), syncRes.getReqDesc());
        }

        String tokenNo = syncRes.getTokenNo();
        String applyNo = syncRes.getApplyNo();
        // 这里是汇付客户id,非平台id
        huifuId = syncRes.getHuifuId();

        // 将两次请求返回的信息封装到commitInfo
        CommitInfoExt commitInfoExt = TransferUtils.transfer(saveCommand.getInfoExt(), CommitInfoExt.class);

        commitInfoExt.setBindCardCashSeqId(tokenNo);
        commitInfoExt.setHuifuId(huifuId);

        saveCommand.setInfoExt(commitInfoExt);

        // 新增三方账号信息
        AccountTripartitePurseVO tripartitePurseVO = buildCommonTripartitePurse(huifuId, accountId, cardReq.getCardNo(), syncRes.isSuccess());
        tripartitePurseVO.setAccountName(accountReq.getName());
        tripartitePurseVO.setCommitInfo(JSONUtil.toJsonStr(saveCommand));
        tripartitePurseVO.setOidApplySeqNo(applyNo);

        tripartitePurseDomain.addAccountTripartitePurse(tripartitePurseVO);

        // TODO
        UserApplyAccountRes userApplyAccountResult = new UserApplyAccountRes();

        return userApplyAccountResult;
    }

    private boolean checkSave(String oidUserNo) {
        Long accountId = SecurityUtils.getAccountId();
        boolean doUpdateAction = StrUtil.isNotBlank(oidUserNo);
        // 根据账号查询
        AccountTripartitePurseVO dbTripartitePurse = tripartitePurseDomain.queryAccountTripartitePurse(accountId);
        // 新增判断
        if (dbTripartitePurse != null && !doUpdateAction) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA);
        }
        // 修改判断
        if (dbTripartitePurse == null && doUpdateAction) {
            throw new PlatformException(BaseErrorCode.NODATA);
        }
        return doUpdateAction;
    }

    private AccountTripartitePurseVO buildCommonTripartitePurse(String oidUserNo, Long accountId, String bankNo, boolean isAllDone) {
        AccountTripartitePurseVO tripartitePurseVO = new AccountTripartitePurseVO();
        tripartitePurseVO.setAccountId(accountId);
        tripartitePurseVO.setOidUserNo(oidUserNo);
        // 绑卡异步，所以这里是绑卡同步中. 有可能直接成功,就直接修改为正常状态
        tripartitePurseVO.setUserStatus(isAllDone ? PurseEnum.TripartitePurchaseStatus.NORMAL : PurseEnum.TripartitePurchaseStatus.BIND_SYNC);
        tripartitePurseVO.setAmount(Money.ZERO);
        tripartitePurseVO.setAccountType(PurseEnum.TripartitePurchasePlatform.HUI_FU);
        tripartitePurseVO.setBankNo(bankNo);
        return tripartitePurseVO;
    }
}
