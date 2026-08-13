package com.newzkl.platform.base.biz.account.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
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

    Page<ChannelVO> channelPage(ChannelQuery channelQuery);

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

    List<SupplierRelationVO> supplierRelationVO(List<Long> supplierIdList);

    ChannelEarningsConfigVO serviceFeeConfigVO(Long channelId);

    /**
     * app账号详情
     *
     */
    AppAccountVO appVO(AccountKeyQuery query);

    Page<ChannelPageRes> queryChannelPage(ChannelQuery channelQuery);

    /**
     * 运营商供应商分页
     */

    Page<SupplierRes> supplierPage(SupplierQuery query);
}
