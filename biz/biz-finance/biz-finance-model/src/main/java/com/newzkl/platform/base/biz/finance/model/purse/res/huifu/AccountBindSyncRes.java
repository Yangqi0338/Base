package com.newzkl.platform.base.biz.finance.model.purse.res.huifu;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.finance.model.enums.finance.HuifuEnum;
import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description:
 * @date 2025-08-25 15:32:54
 */
@Data
public class AccountBindSyncRes extends TripartiteAccountBaseRes {

    private String tokenNo;

    private String applyNo;

    private List<RespBusiness> respBusiness;

    public boolean isSuccess() {
        if (CollUtil.isEmpty(this.getRespBusiness())) {
            return false;
        }
        return super.isSuccess() && this.getRespBusiness().stream().allMatch(it -> HuifuEnum.BizCodeEnum.isSuccess(it.getCode()));
    }

    @Data
    public static class RespBusiness {
        private String type;
        private String code;
        private String msg;
    }
}
