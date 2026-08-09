package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.enums.account.OperatorEnum;
import lombok.Data;

/**
 * 运营商
 *
 * @author fang
 */
@Data
@TableName
public class OperatorDO extends OperatorClientBaseDO {
    /**
     * 运营类型
     */
    private OperatorEnum.Type type;
    /**
     * 行业id / 区域地址编码
     * @ext 多选, 拼接
     */
    private String typeForeignId;
    /**
     * 行业名称 / 区域地址
     */
    private String typeForeignName;
    /**
     * 域名
     */
    private String domain;
    /**
     * 杠杆比例
     */
    private Integer leverageRatio;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 首页logo
     */
    private String logo;

    /**
     * 首页背景图
     */
    private String background;
}