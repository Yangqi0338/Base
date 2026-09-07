package com.newzkl.platform.base.biz.account.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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


    SupplierVO supplierVO(Long supplierId);

    List<SupplierRelationVO> supplierRelationVO(List<Long> supplierIdList);

    ChannelEarningsConfigVO serviceFeeConfigVO(Long channelId);

    /**
     * 运营商供应商分页
     */

    Page<SupplierRes> supplierPage(SupplierQuery query);
}
