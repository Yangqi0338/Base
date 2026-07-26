package com.newzkl.platform.base.biz.account.application.service;


import com.newzkl.platform.base.biz.account.model.vo.ServiceFeeConfigVO;
import com.newzkl.platform.base.biz.account.model.vo.PromiseFlowVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.account.model.req.OperatorReq;
import com.newzkl.platform.base.biz.account.model.req.RoleApplyCommand;
import com.newzkl.platform.base.biz.account.model.cdk.req.ToCdkCommand;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/4/814:28
 */
public interface IdentityService {
    /**
     * 申请角色
     *
     * @param roleApplyCommand
     */
    Long applyRole(RoleApplyCommand roleApplyCommand);

    void updateAuditState(Long accountId, RoleEnum.CompanyRole role);

    /**
     * 保存角色申请资料。
     *
     * <p>迁移补充: 旧 {@code IRoleService.saveApplyCommand}。保留旧语义 —— 当前账号尚未开通
     * 该申请角色时, 旧实现会转入 {@code saveRole}, 而该方法三个分支均直接抛异常
     * (渠道商 / 供应商 抛"无此服务", 其余角色抛"参数异常"), 故此路径不会落缓存;
     * 仅当账号已存在该角色记录时, 申请资料才写入缓存。</p>
     *
     * @param roleApplyCommand 角色申请资料
     * @author KC
     */
    void saveApplyCommand(RoleApplyCommand roleApplyCommand);

    /**
     * 加载角色申请资料。
     *
     * <p>迁移补充: 旧 {@code IRoleService.loadApplyCommand}, 按 (登录账号, 角色) 读缓存。</p>
     *
     * @param roleId 角色 ID
     * @return 角色申请资料, 无则 null
     * @author KC
     */
    RoleApplyCommand loadApplyCommand(Long roleId);

    /**
     * 分配开通码。
     *
     * <p>迁移补充: 旧 {@code IRoleService.toCdk}。领域层完成归属变更后校验影响行数,
     * 再按 (分配人角色, 被分配人角色) 计算期权单价并经资金域出站端口发放期权。</p>
     *
     * @param toCdkCommand 分配命令
     * @author KC
     */
    void toCdk(ToCdkCommand toCdkCommand);

    /**
     * 提交保证金缴纳流水
     *
     * @param promiseFlowVO
     */
    Long submitPromiseFlow(PromiseFlowVO promiseFlowVO);

    /**
     * 服务费修改
     *
     * @param accountId
     * @param serviceFeeConfigVO
     */
    void serviceFeeConfigEdit(Long accountId, ServiceFeeConfigVO serviceFeeConfigVO);

    /**
     * 渠道商上级交易师修改
     *
     * @param channelId
     * @param dealerId
     */
    void channelUpEdit(Long channelId, Long dealerId);

    /**
     * 运营商修改
     *
     * @param operatorCommand
     */
    void operatorEdit(OperatorReq operatorCommand);

    /**
     * 查询服务费
     *
     * @param channelId
     */
    ServiceFeeConfigVO queryServiceFeeConfig(Long channelId);


}
