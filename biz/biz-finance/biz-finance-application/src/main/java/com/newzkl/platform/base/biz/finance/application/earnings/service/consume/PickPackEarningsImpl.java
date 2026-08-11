package com.newzkl.platform.base.biz.finance.application.earnings.service.consume;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONUtil;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.finance.domain.adapt.repository.AccountContributeRepository;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.ConsumeEarnings;
import com.newzkl.platform.base.biz.finance.domain.earnings.service.EarningStrategySupport;
import com.newzkl.platform.base.biz.finance.model.earnings.req.AlterAccountContributeDataReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningsPackExecReq;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.PackOrderRpcVO;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.PickPackInfoVO;
import com.newzkl.platform.base.biz.finance.model.support.api.UpIdRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.newzkl.platform.base.common.ddd.facade.PermissionRpcVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼包分润
 *
 * @author niu
 * @date 2023/12/18 17:54
 */
@Service
public class PickPackEarningsImpl extends EarningStrategySupport implements ConsumeEarnings<EarningsPackExecReq> {

    @Autowired
    private AccountApi accountApi;
    @Autowired
    private AccountContributeRepository accountContributeRepository;

    @Override
    public List<EarningRecordReq> earnings(EarningsPackExecReq req, List<AlterAccountContributeDataReq> contributeDataReqs) {
        List<EarningRecordReq> earningInfos = new ArrayList<>();
        // 1、rpc请求甄选师礼包购买信息
        PackOrderRpcVO packOrder = consumeEarningDataRepository.queryPickPackOrder(req.getOrderNo());
        Long accountId = packOrder.getAccountId();
        Money amount = packOrder.getAmount();

        // 2、获取上下级信息
        UpIdRes upIdRes = consumeEarningDataRepository.upId(CommonEnum.Client.OPERATOR, accountId);
        if (upIdRes == null || upIdRes.getOneId() == null) {
            return earningInfos;
        }
        // 拿直属上级的id
        Long directId = upIdRes.getOneId();
        RoleEnum.CompanyRole directRole = upIdRes.getDirectRoleId();
        PermissionRpcVO permissionVO = accountApi.levelPermissionVO(directRole, 1);
        if (permissionVO == null || permissionVO.getDirectConfig() == null) {
            return earningInfos;
        }

        // 计算分润金额
        double ratio = getRatio(directId, packOrder, permissionVO);

        Money shareAmount = amount.percent(ratio);
        if (shareAmount.greaterThanZero()) {
            return earningInfos;
        }

        // 根据消费类型获取对应收益角色code
        RoleEnum.CompanyRole role = packOrder.getPackType();
        PurseEnum.FinanceUser financeUser = PurseEnum.FinanceUser.getByRole(role);

        // 更新客户贡献数据
        List<AlterAccountContributeDataReq> alterAccountContributeDataReqs = new ArrayList<>();
        alterAccountContributeDataReqs.add(AlterAccountContributeDataReq.buildAmount(accountId, financeUser, amount));
        alterAccountContributeDataReqs.add(AlterAccountContributeDataReq.buildEarning(directId, directId, financeUser, accountId, shareAmount));

        EarningRecordReq earningRecordReq = buildEarningsByInviteId(directId, directRole, req, shareAmount);
        earningRecordReq.setContributeId(accountId);
        // TODO
//        earningRecordReq.setContributeName("");
        earningRecordReq.setGoodsInfo(JSONUtil.toJsonStr(buildPackInfoVo(packOrder, ratio)));
        earningRecordReq.setAlterType(PurseEnum.PurseAlterType.EARNING);
        earningRecordReq.setState(EarningsEnum.State.FINISH);
        earningInfos.add(earningRecordReq);

        accountContributeRepository.alterAccountContribute(alterAccountContributeDataReqs);
        return earningInfos;
    }

    public EarningRecordReq buildEarningsByInviteId(Long accountId, RoleEnum.CompanyRole role, EarningsPackExecReq req, Money amount) {
        EarningsEnum.EarningType earningType = EarningsEnum.EarningType.getByRole(consumeType(), role);
        if (earningType == null) {
            throw new PlatformException(BaseErrorCode.PARAM.getCode(), "不可用的分润类型");
        }
        EarningRecordReq earningRecordReq = new EarningRecordReq();

        earningRecordReq.setAmount(amount);
        earningRecordReq.setEarningType(earningType);
        earningRecordReq.setAccountId(accountId);
        // TODO
//            earningRecordReq.setAccountName("");

        earningRecordReq.setJoinOrderNo(req.getOrderNo());
        earningRecordReq.setPurseType(PurseEnum.PurseType.PACK_INCOME);

        return earningRecordReq;
    }

    /**
     * 获取分润比例
     *
     */
    public Double getRatio(Long accountId, PackOrderRpcVO packOrder, PermissionRpcVO permissionVO) {
        Long id = packOrder.getId();

        // 获取accountId最新序号和任务id Redisson
        Integer nextSeq = getNextLevelSeq(accountId);

        PermissionRpcVO.DirectConfig directConfig = permissionVO.getDirectConfig();

        List<Double> directPack = directConfig.getActiveDirectPack();
        int seq = nextSeq % Math.max(directPack.size(), 1);
        Double ratio = CollUtil.get(directPack, seq);
        if (ratio == null) {
            return 0.0;
        }

//        UserTaskRes userTask = new UserTaskRes();
//        userTask.setType(EarningsEnum.ConsumeType.PICK_PACK);
//        userTask.setSeq(seq);
//        userTask.setForeignId(id);
//        userTask.setAccountId(accountId);
//        userTask.setRatio(ratio);
//        userTask.setCount(nextSeq);
//        userTask.setStatus("1");
//        userTaskFacade.insert(userTask);

        return ratio;
    }

    /**
     * 获取下一个排序序号，基于Redisson的分布式锁和原子操作
     */
    @DistributedLock
    private Integer getNextLevelSeq(Long accountId) {
        // 修改为数据库查询,因为mq的重试会导致redis循环出错
//        UserTaskQuery query = new UserTaskQuery();
//        query.setAccountId(accountId);
//        query.setType(EarningsEnum.ConsumeType.PICK_PACK.getType());
//        UserTaskVO userTaskVO = userTaskFacade.lastTask(query);
//        return Opt.ofNullable(userTaskVO).map(UserTaskVO::getCount).orElse(-1) + 1;
        return 0;
    }


    @Override
    public EarningsEnum.ConsumeType consumeType() {
        return EarningsEnum.ConsumeType.PICK_PACK;
    }

    private PickPackInfoVO buildPackInfoVo(PackOrderRpcVO packOrder, double directSelectorPack) {
        PickPackInfoVO pickPackInfoVO = new PickPackInfoVO();
        pickPackInfoVO.setAmount(packOrder.getAmount());
        pickPackInfoVO.setPackType(packOrder.getPackType());
        pickPackInfoVO.setPackLevel(packOrder.getPackLevel());
        pickPackInfoVO.setPackName(packOrder.getPackName());
        pickPackInfoVO.setRatio(directSelectorPack);
        return pickPackInfoVO;
    }
}
