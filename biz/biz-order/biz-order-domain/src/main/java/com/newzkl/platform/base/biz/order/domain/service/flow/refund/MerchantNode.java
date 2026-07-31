package com.newzkl.platform.base.biz.order.domain.service.flow.refund;


import com.newzkl.platform.base.biz.order.domain.service.flow.model.NextLine;
import com.newzkl.platform.base.biz.order.domain.service.flow.model.Node;
import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/159:12
 */
@Data
public class MerchantNode  implements Node {
    @Override
    public List<NextLine> getNext() {
        return null;
    }
}
