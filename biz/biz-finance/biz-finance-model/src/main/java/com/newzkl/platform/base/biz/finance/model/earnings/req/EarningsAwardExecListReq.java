package com.newzkl.platform.base.biz.finance.model.earnings.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 奖励分润批量执行请求
 *
 * @author niu
 * @date 2023/12/18 16:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EarningsAwardExecListReq extends EarningsExecReq {

    /** 列表 */
    private List<EarningsAwardExecReq> list;

}
