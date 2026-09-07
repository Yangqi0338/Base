package com.newzkl.platform.base.biz.account.model.dto;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.ChannelEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 渠道商纯净视图
 *
 * <p>字段与 {@code channel} 表一一对应, 不含任何副数据。用于只需渠道商自有列的场景
 * (校验入驻状态、取店铺地址等), 相比 {@link com.newzkl.platform.base.biz.account.model.res.ChannelRes}
 * 少一次 account 查询</p>
 *
 * <p>不加 {@code @Builder}: 本类 {@code extends BaseRes} 取 id/createTime 等公共列,
 * Lombok builder 不含父类字段会误导调用方; 且实例统一由 {@code TransferUtils} / MapStruct 产出,
 * 不走 builder</p>
 *
 * @author KC
 * @ext 主数据 channel (无副数据)
 */
@Data
public class ChannelDTO extends BaseRes {

    /**
     * 渠道商名称
     */
    private String name;
    /**
     * 入驻状态 (APPLY 申请中 / IN 已入驻 / OPEN 已开通)
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
