package com.newzkl.platform.base.biz.user.model.relation.vo;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 等级权限值对象
 *
 * @author muc_fang
 */
@Data
public class PermissionVO implements Serializable {

    /**
     * 直推奖配置
     */
    private DirectConfig directConfig;

    /**
     * 直推奖配置
     */
    @Data
    public static class DirectConfig implements Serializable {
        /**
         * 直接下属购买礼包收益开关
         */
        private boolean directPackBoolean;
        /**
         * 直接下属购买礼包收益百分比
         */
        private List<Double> directPack;
        /**
         * 收益分配类型 0 固定比例 1 循环3推1
         */
        private Integer directPackType;

        /**
         * 直接下属供应商订单金额收益开关
         */
        private boolean directSupplierOrderBoolean;
        /**
         * 直接下属供应商订单金额百分比
         */
        private double directSupplierOrder;

        /**
         * 校验直推礼包配置是否有效
         *
         * @return 是否有效
         */
        public boolean checkActiveDirectPack() {
            return (getDirectPackType() == 0 && CollUtil.size(getDirectPack()) > 0) || (getDirectPackType() == 1 && CollUtil.size(getDirectPack()) >= 3);
        }
    }

}
