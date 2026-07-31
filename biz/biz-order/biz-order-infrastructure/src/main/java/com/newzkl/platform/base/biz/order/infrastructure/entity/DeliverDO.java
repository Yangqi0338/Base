package com.newzkl.platform.base.biz.order.infrastructure.entity;

//import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
//import com.gitee.sunchenbin.mybatis.actable.annotation.Index;
//import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
//import com.gitee.sunchenbin.mybatis.actable.annotation.TableComment;
//import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 发货
 * @date 2023/5/417:37
 */
@Data
@TableName
public class DeliverDO extends BaseDO implements Serializable {


    private Long spuOrderId;
    private String deliverUsername;
    /**
     * 物流公司名称
     */
    private String expressCompanyName;

    private String expressNo;
    private String expressMobile;

    private String item;
}
