package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.enums.account.OperatorEnum;
import com.newzkl.platform.base.biz.account.model.vo.OperatorClientBaseVO;
import lombok.Data;

/**
 * 市场运营商
 *
 * @author fang
 */
@Data
public class OperatorOutRes extends OperatorClientBaseVO {
    /**
     * 交易师数量
     */
    private Integer dealerNumber;
    /**
     * 一级市场数量
     */
    private Integer oneMarketNumber;
    /**
     * 二级市场数量
     */
    private Integer twoMarketNumber;
    /**
     * 服务费配置
     */
    private String serviceFeeConfigVO;
    /**
     * 服务费
     */
    private Integer serviceAmount;
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
}