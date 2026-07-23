package com.zkl.scm.finance.application.earnings.service.consume;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.zkl.scm.finance.domain.earnings.service.EarningStrategySupport;
import com.zkl.scm.finance.domain.earnings.service.IConsumeEarnings;
import com.zkl.scm.finance.domain.purse.service.IWithdrawDomain;
import com.zkl.scm.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.zkl.scm.finance.model.earnings.req.EarningRecordReq;
import com.zkl.scm.finance.model.earnings.req.EarningsPaymentExecReq;
import com.zkl.scm.finance.model.earnings.vo.GoodsInfoVO;
import com.zkl.scm.finance.model.purse.vo.ConfigWithdrawVO;
import com.zkl.scm.model.enums.CommonEnum;
import com.zkl.scm.model.enums.finance.EarningsEnum;
import com.zkl.scm.model.enums.finance.PurseEnum;
import com.zkl.scm.model.enums.user.identity.RoleEnum;
import com.zkl.scm.user.model.account.res.AccountInfo;
import com.zkl.scm.user.model.account.res.UpIdRes;
import com.zkl.scm.user.model.relation.vo.PermissionRpcVO;
import com.zkl.scm.user.rpc.facade.IAccountFacade;
import com.zkl.scm.user.rpc.facade.ILevelFacade;
import com.zkl.scm.util.biz.ScmUtil;
import com.zkl.scm.util.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分润
 *
 * @author niu
 * @description: 商品分润
 * @date 2023/12/18 17:53
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsEarningsImpl extends EarningStrategySupport implements IConsumeEarnings<EarningsPaymentExecReq> {

    private final IWithdrawDomain withdrawDomain;

    @DubboReference
    private ILevelFacade levelFacade;

    @DubboReference
    private IAccountFacade accountFacade;

    @Override
    public List<EarningRecordReq> earnings(EarningsPaymentExecReq req, List<AlterAccountContributeDataReq> contributeDataReqs) {
        List<EarningRecordReq> earningInfos = new ArrayList<>();
        // 1、将金额返还到采购账号
        // 2、将
        // 供应商分润
        req.setSource(PurseEnum.FinanceUser.SUPPLIER);
        doSupplierEarnings(earningInfos, req, contributeDataReqs);

        // 渠道商分润
        req.setSource(PurseEnum.FinanceUser.CHANNEL);
        doChannelEarnings(earningInfos, req, contributeDataReqs);

        // 分润记录全部设置为待结算
        earningInfos.forEach(earningInfo -> {
            earningInfo.setState(EarningsEnum.State.SETTLE);
            earningInfo.setAlterType(PurseEnum.PurseAlterType.EARNING);
        });
        return earningInfos;
    }

    /**
     * 运营商侧 邀请人级差分润
     */
    private void doInvitedEarnings(List<EarningRecordReq> earningInfos, EarningsPaymentExecReq req, List<AlterAccountContributeDataReq> alterAccountContributeDataReqs) {
        // 查询邀请人
        Long contributeId = req.getContributeId();
        Long invitedId = req.getInvitedId();
        // 可分润金额
        Integer earningAmount = req.getAmount();
        if (invitedId == null) {
            return;
        }
        CommonEnum.Client client = CommonEnum.Client.OPERATOR;
        // 存在邀请人
        Integer reducedAmount = 0;

        // 查询运营商上下级
        UpIdRes upIdRes = accountFacade.upId(client, invitedId);

        EarningRecordReq earningRecordReq;

        // 获取从下到上的角色排序
        List<RoleEnum.CompanyRole> earningUserRoleId = UpIdRes.getOperatorEarningRole(upIdRes);
        List<Long> upId = CollUtil.newArrayList(invitedId);
        CollUtil.addAll(upId, CollUtil.reverse(upIdRes.getUpId()));

        Long parentId = null;
        for (int i = 0, earningUserRoleIdSize = earningUserRoleId.size(); i < earningUserRoleIdSize; i++) {
            RoleEnum.CompanyRole role = CollUtil.get(earningUserRoleId, i);
            Long accountId = CollUtil.get(upId, i);
            // 若那一层没获取到role和id, 就跳过
            if (role == null || accountId == null) continue;

            // 查询分润配置
            PermissionRpcVO permissionVO = levelFacade.levelPermissionVO(role, 1);
            // 级差分润
            earningRecordReq = stepDiffEarning(permissionVO, reducedAmount, req);
            if (earningRecordReq == null) {
                continue;
            }
            earningRecordReq.setAccountId(accountId);
            // TODO
//            earningRecordReq.setAccountName("");

            earningInfos.add(TransferUtils.transfer(earningRecordReq, EarningRecordReq::new));
            Integer shareAmount = earningRecordReq.getAmount();
            reducedAmount += shareAmount;

            // 渠道商分润贡献数据
            PurseEnum.FinanceUser financeUser = PurseEnum.FinanceUser.getByRole(role);
            // 给对应的账号添加分润金额
            alterAccountContributeDataReqs.add(AlterAccountContributeDataReq.buildEarning(accountId, parentId, financeUser, contributeId, shareAmount));
            parentId = accountId;

            PermissionRpcVO.DirectConfig directConfig = permissionVO.getDirectConfig();

            // 若无勾选服务费配置，则跳过服务费分润
            if (!directConfig.isOrderRatioBoolean()) {
                continue;
            }
            double directSupplierOrder = directConfig.getOrderRatio();
            // 计算供应商服务费贡献数据
            Integer supplierShareAmount = ScmUtil.percentFloor(earningAmount, directSupplierOrder);
            if (supplierShareAmount > 0) {
                alterAccountContributeDataReqs.add(AlterAccountContributeDataReq.buildPlatformServiceAmount(contributeId, PurseEnum.FinanceUser.SUPPLIER,
                        supplierShareAmount));
            }
        }
    }

    /**
     * 渠道商分润
     */
    private void doChannelEarnings(List<EarningRecordReq> earningInfos, EarningsPaymentExecReq req, List<AlterAccountContributeDataReq> alterAccountContributeDataReqs) {
        Long contributeId = req.getContributeId();

        // 查询贡献人的上级
        AccountInfo accountInfo = accountFacade.accountInfo(req.getSource().getRole().getClient().getCode(), contributeId);
        Long inviteAccountId = accountInfo.getInviteAccountId();

        req.setContributeName(accountInfo.getNickname());

        // 1、返还渠道商冻结金额 (贡献已在前面的渠道商支付设置,遂这里不用加贡献)
        // 分润类型设置为空,就不会录入分润
        EarningRecordReq rollBackEarningRecordReq = buildEarningReq(req, null);
        rollBackEarningRecordReq.setPurseType(PurseEnum.PurseType.PURCHASE);
        rollBackEarningRecordReq.setAmount(req.getTotalAmount() - req.getServiceAmount());
        rollBackEarningRecordReq.setAccountId(req.getContributeId());
        rollBackEarningRecordReq.setAccountName(req.getContributeName());
        earningInfos.add(rollBackEarningRecordReq);
        // 2、商品利润
        EarningRecordReq earningRecordReq = buildEarningReq(req, EarningsEnum.EarningType.getByRole(this.consumeType(), req.getSource()));
        earningRecordReq.setAmount(req.getAmount());
        earningRecordReq.setAccountId(req.getContributeId());
        earningRecordReq.setAccountName(req.getContributeName());
        earningInfos.add(earningRecordReq);
        // 3、服务费贡献
        alterAccountContributeDataReqs.add(
                AlterAccountContributeDataReq.buildPlatformServiceAmount(
                        contributeId, PurseEnum.FinanceUser.CHANNEL, req.getServiceAmount()
                )
        );

        // TODO 渠道商暂时不做邀请人分润
        if (inviteAccountId != null) {
            req.setInvitedId(inviteAccountId);
            //        doInvitedEarnings(earningInfos, req, alterAccountContributeDataReqs);
        }
    }

    /**
     * 供应商分润
     */
    private void doSupplierEarnings(List<EarningRecordReq> earningInfos, EarningsPaymentExecReq req, List<AlterAccountContributeDataReq> alterAccountContributeDataReqs) {
        Long contributeId = req.getContributeId();
        Integer amount = req.getAmount();

        // 供应商贡献
        alterAccountContributeDataReqs.add(
                AlterAccountContributeDataReq.buildAmount(
                        contributeId, PurseEnum.FinanceUser.SUPPLIER, amount
                )
        );

        // 有上级就做邀请人极差分润
        AccountInfo accountInfo = accountFacade.accountInfo(req.getSource().getRole().getClient().getCode(), contributeId);
        Long inviteAccountId = accountInfo.getInviteAccountId();
        if (inviteAccountId != null) {
            ConfigWithdrawVO configWithdrawVO = withdrawDomain.defaultWithdrawConfig();
            Integer orderEarningRatio = configWithdrawVO.getOrderEarningRatio();
            req.setMaxEarningsPrecent(orderEarningRatio);

            req.setInvitedId(inviteAccountId);
            req.setContributeName(accountInfo.getNickname());
            doInvitedEarnings(earningInfos, req, alterAccountContributeDataReqs);
        }

    }

    private EarningRecordReq buildEarningReq(EarningsPaymentExecReq req, EarningsEnum.EarningType earningType) {
        // 订单的铺货价-销售价
        EarningRecordReq earningRecordReq = new EarningRecordReq();
        earningRecordReq.setEarningType(earningType);
        earningRecordReq.setJoinOrderNo(req.getId());
        earningRecordReq.setContributeId(req.getContributeId());
        earningRecordReq.setContributeName(req.getContributeName());
        GoodsInfoVO goodsInfoVO = buildGoodsInfo(req);
        earningRecordReq.setGoodsInfo(JSONUtil.toJsonStr(goodsInfoVO));
        earningRecordReq.setPurseType(PurseEnum.PurseType.GOODS_INCOME);

        return earningRecordReq;
    }

    /**
     * 级差分润
     *
     * @param permission    当前级分润配置
     * @param reducedAmount 已分走的金额
     * @param req      订单信息
     *
     */
    private EarningRecordReq stepDiffEarning(PermissionRpcVO permission, Integer reducedAmount, EarningsPaymentExecReq req) {
        PermissionRpcVO.DirectConfig directConfig = permission.getDirectConfig();

        Integer amount = req.getAmount();

        // 若无勾选分润配置，则跳过分润
        if (!directConfig.isDirectSupplierOrderBoolean() || amount == null) {
            return null;
        }

        // 不能超过最大分润比例
        double orderRatio = Math.min(directConfig.getDirectSupplierOrder(), req.getMaxEarningsPrecent());

        // 计算分润金额, (级差,减去已分润的金额,若小于1,不能分润直接返回)
        int shareAmount = ScmUtil.percentFloor(amount, orderRatio) - reducedAmount;
        if (shareAmount < 1) {
            return null;
        }

        EarningsEnum.EarningType earningType = EarningsEnum.EarningType.getByRole(this.consumeType(), permission.getRole());
        EarningRecordReq earningRecordReq = buildEarningReq(req, earningType);
        earningRecordReq.setAmount(amount);
        return earningRecordReq;
    }


    private GoodsInfoVO buildGoodsInfo(EarningsPaymentExecReq req) {
        GoodsInfoVO goodsInfoVO = req.getGoodsInfoVO();
        goodsInfoVO.setOutId(req.getContributeId());
        goodsInfoVO.setOutName(req.getContributeName());
        return goodsInfoVO;
    }

    @Override
    public EarningsEnum.ConsumeType consumeType() {
        return EarningsEnum.ConsumeType.GOODS;
    }
}
