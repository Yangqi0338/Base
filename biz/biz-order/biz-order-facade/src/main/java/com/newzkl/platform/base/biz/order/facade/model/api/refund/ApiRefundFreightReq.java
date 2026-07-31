package com.newzkl.platform.base.biz.order.facade.model.api.refund;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 提交退货物流参数
 * @author fang
 */
@Data
public class ApiRefundFreightReq  implements Serializable {
    /**
     * 退货ID
     */
    @NotNull
    private Long refundId;
    /**
     * 物流公司名称 (填写: 顺丰 、顺丰速运、shunfeng 皆可)
     * ("顺丰速运","shunfeng"),
     * ("极兔速递","jtexpress"),
     * ("圆通速递","yuantong"),
     * ("中通快递","zhongtong"),
     * ("申通快递","shentong"),
     * ("韵达快递","yunda"),
     * ("百世快递","huitongkuaidi"),
     * ("德邦快递","debangwuliu"),
     * ("全峰快递","quanfengkuaidi"),
     * ("天天快递","tiantian"),
     * ("速尔快递","suer"),
     * ("优速快递","youshuwuliu"),
     * ("天地华宇","tiandihuayu"),
     * ("邮政快递","youzhengguonei"),
     * ("安鲜达","exfresh"),
     * ("日日顺物流","rrs"),
     * ("国际包裹","youzhengguoji"),
     * ("京东物流","jd"),
     * ("苏宁物流","suning"),
     * ("货拉拉物流","huolalawuliu"),
     * ("万象物流","wanxiangwuliu"),
     * ("安能快运","annengwuliu"),
     * ("信丰物流","xinfengwuliu"),
     * ("宅急送","zhaijisong"),
     * ("国通快递","guotongkuaidi"),
     * ("EMS","ems"),
     * ("全一快递","quanyikuaidi"),
     * ("中铁快运","ztky"),
     * ("品骏快递","pjbest"),
     * ("黄马甲","huangmajia"),
     * ("晟邦物流","nanjingshengbang"),
     * ("安得物流","annto"),
     * ("加运美","jiayunmeiwuliu"),
     * ("转运四方","zhuanyunsifang"),
     * ("壹米滴答","yimidida"),
     * ("芝麻开门","zhimakaimen"),
     * ("跨越速运","kuayue"),
     * ("顺心捷达","sxjdfreight"),
     * ("特急送","lntjs"),
     * ("九曳供应链","jiuyescm"),
     * ("丹鸟","danniao"),
     * ("速必达","subida"),
     * ("京广速递","jinguangsudikuaijian"),
     * ("联昊通","lianhaowuliu"),
     * ("丰通快运","ftky365"),
     * ("三志物流","sanzhi56"),
     * ("海信物流","savor"),
     */
    @NotEmpty
    private String freightCompanyName;
    /**
     * 物流单号
     */
    @NotEmpty
    private String freightNo;
}
