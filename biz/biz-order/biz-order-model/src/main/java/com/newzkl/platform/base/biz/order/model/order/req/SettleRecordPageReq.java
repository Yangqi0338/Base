package com.newzkl.platform.base.biz.order.model.order.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleRecordVO;
import lombok.Data;

import java.util.List;

/**
* 结算记录表
* @author fang
*/
@Data
public class SettleRecordPageReq extends Page<SettleRecordVO> {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 供应商ID
     */
    private Long supplierId;
}
