package com.newzkl.platform.base.biz.account.model.level.vo;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 等级权限值对象 (DB 以 JSON 列存储)。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.level.model.vo.PermissionVO}。</p>
 *
 * @author KC
 */
@Data
public class PermissionVO implements Serializable {

    /**
     * 直推奖配置
     */
    private DirectConfig directConfig;

    /**
     * 直推奖配置。
     *
     * @author KC
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
         * 收益分配类型: 0 固定比例 1 循环 3 推 1
         */
        private Integer directPackType;

        /**
         * 直接下属供应商订单金额收益开关
         */
        private boolean directSupplierOrderBoolean;

        /**
         * 直接下属供应商订单金额收益百分比
         */
        private double directSupplierOrder;

        /**
         * 校验直推礼包配置是否有效。
         *
         * <p>固定比例需至少 1 档; 循环 3 推 1 需至少 3 档。</p>
         *
         * @return true 表示配置有效
         */
        public boolean checkActiveDirectPack() {
            if (directPackType == null) {
                return false;
            }
            if (directPackType == 0) {
                return CollUtil.size(directPack) > 0;
            }
            return directPackType == 1 && CollUtil.size(directPack) >= 3;
        }
    }
}
