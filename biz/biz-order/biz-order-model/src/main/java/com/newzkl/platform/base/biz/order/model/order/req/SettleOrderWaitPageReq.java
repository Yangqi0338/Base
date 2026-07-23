package com.newzkl.platform.base.biz.order.model.order.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleOrderWaitVO;
import lombok.Data;

import java.util.List;

/**
* 待结算订单信息表
* @author fang
*/
@Data
public class SettleOrderWaitPageReq extends Page<SettleOrderWaitVO> {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * SPU ID集合
     */
    private List<Long> spuIdList;
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 结算状态
     */
    private Integer settleState;
}
