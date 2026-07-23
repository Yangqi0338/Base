package com.newzkl.platform.base.biz.finance.model.enums.order;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ExpressEnum {
    /**
     * 支付类型
     */
    @Getter
    @AllArgsConstructor
    public enum ExpressType {
        顺丰速运("顺丰速运","shunfeng"),
        圆通速递("圆通速递","yuantong"),
        中通快递("中通快递","zhongtong"),
        申通快递("申通快递","shentong"),
        韵达快递("韵达快递","yunda"),
        百世快递("百世快递","huitongkuaidi"),
        德邦("德邦","debangwuliu"),
        全峰快递("全峰快递","quanfengkuaidi"),
        天天快递("天天快递","tiantian"),
        速尔快递("速尔快递","suer"),
        优速("优速","youshuwuliu"),
        天地华宇("天地华宇","tiandihuayu"),
        邮政快递包裹("邮政快递包裹","youzhengguonei"),
        安鲜达("安鲜达","exfresh"),
        日日顺物流("日日顺物流","rrs"),
        国际包裹("国际包裹","youzhengguoji"),
        京东物流("京东物流","jd"),
        苏宁物流("苏宁物流","suning"),
        货拉拉物流("货拉拉物流","huolalawuliu"),
        万象物流("万象物流","wanxiangwuliu"),
        安能快运("安能快运","annengwuliu"),
        信丰物流("信丰物流","xinfengwuliu"),
        宅急送("宅急送","zhaijisong"),
        国通快递("国通快递","guotongkuaidi"),
        EMS("EMS","ems"),
        全一快递("全一快递","quanyikuaidi"),
        中铁快运("中铁快运","ztky"),
        品骏快递("品骏快递","pjbest"),
        黄马甲("黄马甲","huangmajia"),
        晟邦物流("晟邦物流","nanjingshengbang"),
        安得物流("安得物流","annto"),
        加运美("加运美","jiayunmeiwuliu"),
        转运四方("转运四方","zhuanyunsifang"),
        壹米滴答("壹米滴答","yimidida"),
        芝麻开门("芝麻开门","zhimakaimen"),
        跨越速运("跨越速运","kuayue"),
        韵达快运("韵达快运","yundakuaiyun"),
        顺心捷达("顺心捷达","sxjdfreight"),
        特急送("特急送","lntjs"),
        百世快运("百世快运","baishiwuliu"),
        九曳供应链("九曳供应链","jiuyescm"),
        丹鸟("丹鸟","danniao"),
        速必达("速必达","subida"),
        京广速递("京广速递","jinguangsudikuaijian"),
        联昊通("联昊通","lianhaowuliu"),
        极兔速递("极兔速递","jtexpress"),
        丰通快运("丰通快运","ftky365"),
        三志物流("三志物流","sanzhi56"),
        中通快运("中通快运","zhongtongkuaiyun"),
        邮政电商标快("邮政电商标快","youzhengdsbk"),
        邮政标准快递("邮政标准快递","youzhengbk"),
        顺丰快运("顺丰快运","shunfengkuaiyun"),
        海信物流("海信物流","savor"),
        ;
        private String code;
        private String value;

        public static ExpressEnum.ExpressType getByCodeLike(String code) {
            if(StrUtil.isEmpty(code)){
                return null;
            }
            return Stream.of(ExpressEnum.ExpressType.values())
                    .filter(extension -> extension.code.indexOf(code) >= 0)
                    .findFirst()
                    .orElse(null);
        }
        public static ExpressEnum.ExpressType getByValue(String value) {
            if(StrUtil.isEmpty(value)){
                return null;
            }
            return Stream.of(ExpressEnum.ExpressType.values())
                    .filter(extension -> extension.value.equals(value))
                    .findFirst()
                    .orElse(null);
        }

        public static List<Map<String, String>> listAllWithMap() {
            List<Map<String, String>> list = new ArrayList<>();
            for (ExpressType type : ExpressType.values()) {
                Map<String, String> map = new HashMap<>();
                map.put("code", type.getCode());
                map.put("value", type.getValue());
                // 可选：新增name字段（枚举名称），方便调试
                map.put("name", type.name());
                list.add(map);
            }
            return list;
        }
    }
}
