package com.newzkl.platform.base.biz.finance.model.support;

import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.HuifuEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class TripartiteBaseRes implements Serializable {

    /**
     * 三方单号
     */
    private String tripartiteNo;

    /**
     * 请求状态描述
     */
    private String reqDesc;

    /**
     * 请求状态编码
     */
    private String reqCode;

    /**
     * 请求状态 (1 成功 0 失败)
     */
    private CommonEnum.YesOrNo reqState;

    /**
     * 请求日期
     */
    private String reqDate;

    public <T extends TripartiteBaseRes> T build() {
        if (reqState == null) {
            this.reqState = CommonEnum.YesOrNo.getByBool(HuifuEnum.isSuccess(this.reqCode));
        }
        if (reqDate == null) {
            this.reqDate = DateUtil.date().toString("yyyyMMdd");
        }
        return (T) this;
    }

    public boolean isSuccess() {
        return CommonEnum.YesOrNo.YES == reqState;
    }

}