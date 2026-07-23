package com.newzkl.platform.base.biz.order.model.order.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.vo.SettleGoodsVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
* 结算商品信息表
* @author fang
*/
@Data
public class SettleGoodsPageReq extends Page<SettleGoodsVO> {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 小于结算时间
     */
    private LocalDateTime lessSettleTime;
}
