package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import lombok.Data;

/**
 * 渠道商
 *
 * @author fang
 */
@Data
@TableName
public class ChannelDO extends BaseDO {
    /**
     * 名称
     * @ext 源 account.realname
     */
    private String name;
    /**
     * 状态
     */
    private ChannelEnum.State state;
    /**
     * 企业信息
     */
    private String companyInfo;
    /**
     * 审批拒绝原因
     */
    private String auditRefuseReason;
    /**
     * 平台服务费
     */
    @TableField(exist = false)
    private String platformNowValue;
    /**
     * 数字门店权限
     */
    private CommonEnum.YesOrNo storePermission;
    /**
     * 店铺地址, 省 CODE, 6 位
     */
    private Integer shipProvinceCode;
    /**
     * 店铺地址, 市 CODE, 6 位
     */
    private Integer shipCityCode;
    /**
     * 店铺地址, 区 CODE, 6 位
     */
    private Integer shipAreaCode;
    /**
     * 联系方式
     */
    private String contactsWay;
    /**
     * 联系人名称
     */
    private String contactsName;
    /**
     * 店铺名称
     */
    private String storeName;
}