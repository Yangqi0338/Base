package com.newzkl.platform.base.biz.finance.model.purse.res.huifu;

import lombok.Data;

/**
 * @author niu
 * @description:
 * @date 2025-08-25 15:32:54
 */
@Data
public class AsyncRes<T> extends TripartiteAccountBaseRes {

    private T data;

    @Data
    static class NotifyRes extends TripartiteAccountBaseRes {

        private String reqSeqId;

        private String notifyType;

        private String state;

        private String stateDesc;
    }

}
