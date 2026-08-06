package com.newzkl.platform.base.common.ddd.facade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author niu
 * @description: 运费计算返回对象
 * @date 2023/4/28 15:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FreightCalculateRes implements Serializable {

    public FreightCalculateRes(boolean result, List<Long> noSendGoods) {
        this.result = result;
        this.noSendGoods = noSendGoods;
    }

    public FreightCalculateRes(boolean result, Map<Long, Integer> spuAmount) {
        this.result = result;
        this.spuAmount = spuAmount;
    }

    /**
     * 计算结果
     */
    private boolean result;
    /**
     * 异常信息: 不配送商品  为null则全部支持配送
     */
    private List<Long> noSendGoods;
    /**
     * SPU运费 key:spuID value:金额
     */
    private Map<Long, Integer> spuAmount;
}
