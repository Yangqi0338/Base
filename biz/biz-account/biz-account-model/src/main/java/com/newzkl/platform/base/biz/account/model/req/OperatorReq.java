package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.OperatorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 市场运营商
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperatorReq extends BaseReq {
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 账号名称
     */
    private String username;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 服务费配置
     */
    private String serviceFeeConfigVO;
    /**
     * 服务费 (Money, 落库 BIGINT 分)
     */
    private Money serviceAmount;
    /**
     * 杠杆比例
     */
    private Integer leverageRatio;
    /**
     * 域名
     */
    private String domain;
    /**
     * 信息
     */
    private String info;
    /**
     * 运营类型 0 机构 1 行业 2 区域
     */
    private OperatorEnum.Type type;
    /**
     * 行业id,区域地址编码 | (多选,拼接)
     */
    private String typeForeignId;
    /**
     * 行业名称 / 区域地址
     */
    private String typeForeignName;
    /**
     * 密码
     */
    private String password;
}
