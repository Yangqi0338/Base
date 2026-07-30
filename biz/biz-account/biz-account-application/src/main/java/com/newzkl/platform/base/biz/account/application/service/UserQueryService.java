package com.newzkl.platform.base.biz.account.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.biz.account.model.cdk.req.CdkQuery;
import com.newzkl.platform.base.biz.account.model.cdk.res.CdkRes;
import com.newzkl.platform.base.biz.account.model.req.*;
import com.newzkl.platform.base.biz.account.model.res.*;
import com.newzkl.platform.base.biz.account.model.vo.*;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/2/2210:36
 */
public interface UserQueryService {

    /**
     * 账号外部视图
     *
     * @param client
     * @param userId
     * @return
     */
    AccountOutRes accountOutVO(CommonEnum.Client client, Long userId);

    /**
     * 账号外部视图
     *
     * @param phone
     * @return
     */
    AccountOutRes accountOutVO(String phone);

    /**
     * 获取账号身份信息
     *
     * @param id
     * @param role
     * @return
     */
    Object userIdentityDetail(Long id, RoleEnum.CompanyRole role);

    /**
     * 账号视图 by 邀请码
     *
     * @param yqm
     * @return
     */
    AccountVO accountByYqm(String yqm);

    /**
     * 用户统计信息
     *
     * @return
     */
    UserCountRes userCount();

    /**
     * 用户分组统计
     *
     * @param timeQuery
     * @return
     */
    List<GroupCountRes> groupCount(TimeQuery timeQuery);

    /**
     * 获取账号ID
     *
     * @param username
     * @return
     */
    Long accountIdByUsername(String username);

    /**
     * 获取甄选师账号ID
     *
     * @param username
     * @return
     */
    Long selectorIdByUsername(String username);

    Long selectorPid(Long accountId);

    /**
     * member详情
     *
     * @param memberId
     * @return
     */
    MemberVO memberVO(Long memberId);

    /**
     * member分页
     *
     * @param memberQuery
     * @return
     */
    Page<MemberVO> memberPage(MemberQuery memberQuery);

    List<SupplierDescVO> supplierDescVOList(List<Long> supplierIdList);
    /**
     * countSale分页
     *
     * @param countSaleQuery
     * @return
     */
    Page<CountSaleVO> countSalePage(CountSaleQuery countSaleQuery);

    /**
     * countSale
     *
     * @param todayCountSaleQuery
     * @return
     */
    CountSaleVO countSaleByQuery(CountSaleQuery todayCountSaleQuery);



    Page<ChannelVO> channelPage(ChannelQuery channelQuery);

    int countByQuery(ChannelQuery channelQuery);

    /**
     * 开通码分页
     *
     * <p>迁移补充: 旧 {@code IUserQueryService.cdkPage} 返回 PageHelper 的 {@code PageInfo},
     * 中台统一返回 MyBatis-Plus {@code Page}, 记录类型为出参对象 {@code CdkRes}。</p>
     *
     * @param cdkQuery 开通码查询
     * @return 开通码分页
     * @author KC
     */
    Page<CdkRes> cdkPage(CdkQuery cdkQuery);

    SupplierVO supplierVO(Long supplierId);

    /**
     * 供应商对外视图
     *
     * <p>迁移自旧 {@code IUserQueryService#supplierOutVO}, 供甄选师查看供应商详情用。
     * 旧实现逐字段手工拷贝 {@code SupplierVO} 到 {@code SupplierOutVO}, 中台化后同名字段整体转换。</p>
     *
     * @param supplierId 供应商账号ID
     * @return 供应商对外视图, 无则 null
     * @author KC
     */
    SupplierOutRes supplierOutVO(Long supplierId);

    Page<SelectorSupplierVO> selectorSupplierVO(SupplierQuery supplierQuery);

    List<SupplierRelationVO> supplierRelationVO(List<Long> supplierIdList);

    ChannelEarningsConfigVO serviceFeeConfigVO(Long channelId);

    OperatorVO operatorVO(Long operatorId);

    Page<OperatorVO> operatorPage(OperatorQuery operatorQuery);

    DealerVO dealerVO(Long dealerId);

    Page<DealerVO> dealerPage(DealerQuery dealerQuery);

    SelectorVO selectorVO(Long selectorId);

    Page<SelectorVO> selectorPage(SelectorQuery selectorQuery);

    UpIdRes channelUpId(Long accountId);

    OperatorDomainInfo getOperatorDomainInfoBySub();

    IndexCountRes indexCount();

    String supplierRegistrationInvitationLink(String headHost);

    OperatorVO getOperatorByDomain(String domain);

    /**
     * app账号详情
     *
     */
    AppAccountVO appVO(AccountKeyQuery query);

    Page<ChannelPageRes> queryChannelPage(ChannelQuery channelQuery);

    /**
     * 运营商供应商分页
     */
    Page<SupplierVO> operatorSupplierPage(SupplierQuery supplierQuery);
}
