package com.newzkl.platform.base.biz.finance.model.earnings.req;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 奖励的分润
 *
 * @author niu
 * @description: 分润请求对象
 * @date 2023/12/18 16:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EarningsAwardExecListReq extends EarningsExecReq {

    private List<EarningsAwardExecReq> list;

}
