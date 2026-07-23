package com.newzkl.platform.base.biz.order.model.order.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordItemVO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
* 结算记录明细表
* @author fang
*/
@Data
public class SettleRecordItemPageReq extends Page<SettleRecordItemVO> {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 结算单ID
     */
    @NotNull(message = "结算单ID?")
    private Long settleRecordId;
    /**
     * 商品名称
     */
    private String spuName;
}
