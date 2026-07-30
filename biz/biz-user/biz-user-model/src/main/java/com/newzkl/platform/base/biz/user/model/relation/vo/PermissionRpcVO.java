package com.newzkl.platform.base.biz.user.model.relation.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 等级权限 RPC 视图对象
 *
 * @author muc_fang
 */
@Data
public class PermissionRpcVO implements Serializable {
    /**
     * 直推奖配置
     */
    private DirectConfig directConfig;

    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;

    /**
     * 直推奖配置
     */
    @Data
    public static class DirectConfig implements Serializable {
        /**
         * 下游购买礼包收益开关
         */
        private boolean directPackBoolean;
        /**
         * 下游购买礼包收益百分比
         */
        private List<Double> directPack;
        /**
         * 收益分配类型 0 固定比例 1 循环3推1
         */
        private Integer directPackType;

        /**
         * 下游供应商订单金额收益开关
         */
        private boolean directSupplierOrderBoolean;
        /**
         * 下游供应商订单金额百分比
         */
        private double directSupplierOrder;

        /**
         * 消费比例开关
         */
        private boolean orderRatioBoolean;
        /**
         * 消费比例
         */
        private double orderRatio;

        /**
         * 获取有效的直推礼包收益配置
         *
         * @return 有效收益比例列表
         */
        public List<Double> getActiveDirectPack() {
            List<Double> resList = new ArrayList<>();
            boolean directPackBoolean = this.isDirectPackBoolean();
            if (!directPackBoolean) {
                resList.add(0.0);
            } else {
                List<Double> directPack = this.getDirectPack();
                if (this.getDirectPackType() == 0) {
                    resList.add(Opt.ofNullable(CollUtil.getFirst(directPack)).orElse(0.0));
                } else {
                    resList.addAll(directPack);
                }
            }
            return resList;
        }
    }
}
