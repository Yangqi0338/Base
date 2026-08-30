
package com.newzkl.platform.base.common.ddd.model.enums.goods;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author fang
 */
public class SpuEnum {
    @Getter
    @AllArgsConstructor
    public enum SaleType implements IEnum<Integer> {
        /**
         * 实物
         */
        REAL(0, "实物"),
        /** 课程 */
        COURSE(1, "课程"),
        /** 服务 */
        SERVICE(2, "服务"),
        /** 次卡 */
        NUMBER(3, "次卡"),
        /** 虚拟其他 */
        UNREAL(99, "虚拟其他"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum SpuAttributeType implements IEnum<Integer> {
        /** 销售属性 */
        SALE(0, "销售属性"),
        /** 参数属性 */
        PARAM(1, "参数属性"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum SpuChannelSource implements IEnum<Integer> {
        /** 平台 */
        PLATFORM(0, "平台"),
        /** 怡亚通 */
        YYT(1, "怡亚通"),
        /** 会订货 */
        HDH(2, "会订货"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static SpuChannelSource findByCode(Integer code) {
            return Arrays.stream(SpuChannelSource.values()).filter(it -> it.getCode().equals(code)).findFirst().orElse(null);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum State implements IEnum<Integer> {
        /** 初始化 */
        INIT(-1, "初始化"),
        /** 仓库中 */
        STORE(0, "仓库中"),
        /** 平台下架 */
        PLATFORM_DOWN(1, "平台下架"),
        /** 在售 */
        SALE(2, "在售"),
        /** 供应商下架 */
        DOWN(3, "供应商下架"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum OperateTarget implements IEnum<Integer> {
        /** SPU基础信息 */
        SPU_BASE(1, "SPU基础信息"),
        /** 销售属性 */
        SALE_ATTRIBUTE(2, "销售属性"),
        /** 参数属性 */
        PARAM_ATTRIBUTE(3, "参数属性"),
        /** SKU基础信息 */
        SKU_BASE(4, "SKU基础信息"),
        /** SPU状态 */
        SPU_STATE(5, "SPU状态"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum OperateType implements IEnum<Integer> {
        /** 修改 */
        UPDATE(1, "修改"),
        /** 新增 */
        ADD(2, "新增"),
        /** 删除 */
        DELETE(3, "删除"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum DeliverTimeType implements IEnum<Integer> {
        /** 3日内 */
        DAY_LESS_3(0, "3日内"),
        /** 大于3日 */
        DAY_MORE_3(1, "大于3日"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum ChannelType implements IEnum<Integer> {
        /** 供应商商品 */
        SELECTION(0, "供应商商品"),
        /** 外部供应链商品 */
        OUT(2, "外部供应链商品"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
        public static ChannelType getByCode(Integer code) {
            return Stream.of(ChannelType.values())
                    .filter(it -> it.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum SpecType implements IEnum<Integer> {
        /** 多规格 */
        MULTIPLE(0, "多规格"),
        /** 单规格 */
        SINGLE(1, "单规格"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    //来源
    @Getter
    @AllArgsConstructor
    public enum Source implements IEnum<Integer> {
        /** 市场 */
        MARKET(0, "市场"),
        /** 平台 */
        PLATFORM(1, "平台"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

}
