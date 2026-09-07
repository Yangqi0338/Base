package com.newzkl.platform.base.biz.goods.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import org.dromara.autotable.annotation.Index;

/**
 * 席位套餐数据对象 (DO)
 */
@Data
@TableName
public class SeatPackageDO extends BaseDO {

    /**
     * 席位套餐code
     */
    @Index
    private String seatPackageCode;

    /**
     * 席位套餐名称
     */
    @Index
    private String seatPackageName;

    /**
     * 席位个数
     */
    private Integer seatNum;

    /**
     * 套餐价格
     */
    private Money packagePrice;

    /**
     * 描述
     */
    private String packageDescribe;

    /**
     * 状态
     */
    private CommonEnum.YesOrNo state;
}
