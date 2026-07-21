package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;

import lombok.Data;

import java.util.List;

/**
 * 用户账号
 *
 * @author fang
 */
@Data
public class SubAccountVO extends BaseVO {
    /**
     * 0 直属 1 非直属
     *
     */
    public Integer scope;
    /**
     * 手机号
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 订单供货价金额
     */
    private Integer totalSupplierAmount;
    /**
     * 角色
     */
    private RoleEnum.CompanyRole role;

    public String getRoleName() {
        return role == null ? null : role.getValue();
    }
    /**
     * 下级账号列表
     */
    private List<SubAccountVO> children;
    /**
     * 甄选师人数
     */
    private Integer selectorCount = 0;
    /**
     * 交易师人数
     */
    private Integer dealerCount = 0;
    /**
     * 运营商人数
     */
    private Integer operatorCount = 0;
    /**
     * 渠道商人数
     */
    private Integer channelCount = 0;
    /**
     * 供应商人数
     */
    private Integer supplierCount = 0;

    /**
     * 下级总人数
     */
    public Integer getSubAccountCount() {
        return selectorCount + dealerCount + operatorCount + channelCount + supplierCount;
    }
}