package com.newzkl.platform.base.biz.account.domain.policy;

import com.newzkl.platform.base.common.ddd.model.vo.EditColumnVO;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
// TODO[cross-domain relation]: import com.zkl.scm.user.domain.relation.service.ILevelDomain; (relation biz, 迁 biz-user)
import com.newzkl.platform.base.biz.account.model.req.AccountReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityRegisterRes;
import com.newzkl.platform.base.biz.account.model.vo.AccountStructureTreeVO;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.biz.account.model.assembler.AccountAssembler;
import com.newzkl.platform.base.biz.account.model.assembler.IdentityAssembler;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityCustomSaveReq;
import com.newzkl.platform.base.biz.account.model.auth.req.IdentityProxySaveReq;
// TODO[cross-domain relation]: relation.req.AccountLevelUpReq/ConditionReq/TeamUserCountReq, relation.res.PackGoodsInfo, relation.vo.LevelVO (迁 biz-user)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 角色策略
 * @date 2024/1/911:42
 */
@Component
public abstract class AbsIdentityPolicy {

    @Autowired
    protected AccountDomain accountService;
    // TODO[cross-domain relation]: @Autowired public ILevelDomain levelDomain; (relation biz, 迁 biz-user)
    @Autowired
    public IdentityAssembler identityAssembler;
    @Autowired
    public AccountAssembler accountAssembler;

    /**
     * 支持
     * @return
     */
    public abstract RoleEnum.CompanyRole support();

    /**
     * 个人注册角色
     * @param customSaveReq
     * @return
     */
    public abstract IdentityRegisterRes customRegister(IdentityCustomSaveReq customSaveReq);

    /**
     * 代理注册角色 (平台 or 其他角色)
     *
     * @param proxySaveReq 账号注册参数
     * @return
     */
    public abstract IdentityRegisterRes proxyRegister(IdentityProxySaveReq proxySaveReq);

    /**
     * 邀请别人成功
     *
     * @param account
     * @param inviteAccount
     */
    public abstract void inviteSuccess(AccountVO account, AccountVO inviteAccount, Object roleObj);

    // TODO[cross-domain relation]: levelUp(AccountLevelUpReq,...) 依赖 relation DTO, 迁 biz-user 后恢复

    public void addColumn(List<EditColumnVO> columnList, Long id) {

    }

    // TODO[cross-domain relation]: levelUpCheck(AccountLevelUpReq) 依赖 relation.ConditionReq/PackGoodsInfo/TeamUserCountReq/LevelVO + levelDomain.executeLevel, 迁 biz-user 后恢复
    // 原逻辑: 组装 ConditionReq -> levelDomain.executeLevel 求最大可升级角色。跨 relation 域, 暂移除。

    public Integer findSupplierGoodsCount(Long accountId) {
        return 0;
    }

    /**
     * 获取账号流水数据
     *
     */
    public Integer getOrderTotalAmount(Long accountId) {
        return 0;
    }

    /**
     * 获取升级模式下直属和非直属的下级账号结构
     *
     */
    // TODO[cross-domain relation]: findLevelUpTeamUser(Long,Long) 返回 Map<Integer,List<TeamUserCountReq>> (relation DTO), 迁 biz-user 后恢复
    // 原逻辑: 按直属/非直属分组统计下级账号结构的角色人数, 供升级条件计算

    /**
     * 获取直属和非直属的下级账号结构
     *
     */
    public List<AccountStructureTreeVO> findScopeSubAccountStructure(Long accountId) {
        // 获取Redis 缓存
//        String cacheKey = String.format(CacheKey.AMC_SUB_STRUCTURE, accountId);
//        String structureListStr = redisClient.getCacheObject(cacheKey);
//
        List<AccountStructureTreeVO> res;
//        if (StrUtil.isNotBlank(structureListStr)) {
//            res = JSONUtil.toList(structureListStr, AccountSubStructureVO.class);
//        } else {
            // 无缓存 请求数据库
        res = accountService.findScopeSubAccountStructure(null, accountId);
//            redisClient.setCacheObject(cacheKey, JSONUtil.toJsonStr(res));
//        }

        return res;
    }

    public abstract Object detail(Long id);

    public abstract boolean destroy(AccountVO accountVO, String destroyReason);

    public abstract void saveByAccount(AccountReq req);
}
