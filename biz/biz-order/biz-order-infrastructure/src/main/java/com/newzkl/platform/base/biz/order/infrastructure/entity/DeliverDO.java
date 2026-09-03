package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.order.model.vo.DeliverItemVO;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.io.Serializable;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 发货
 * @date 2023/5/417:37
 */
@Data
@TableName(autoResultMap = true)
public class DeliverDO extends BaseDO implements Serializable {


    /**
     * SPU订单ID
     */
    private String orderNo;

    /**
     * 发货人用户名
     */
    private String deliverUsername;
    /**
     * 物流公司名称
     */
    private String expressCompanyName;

    /**
     * 物流单号
     */
    private String expressNo;

    /**
     * 快递联系电话
     */
    private String expressMobile;

    /**
     * 发货明细
     */
    @JsonSerializable
    private List<DeliverItemVO> item;
}
