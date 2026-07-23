package com.newzkl.platform.base.biz.store.model.template.vo;

import com.newzkl.platform.base.biz.store.model.template.vo.MarketRpcVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author niu
 * @description: 条件列表
 * @date 2024/4/12 9:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionListVO {

    /**
     * 市场条件
     */
    private List<MarketRpcVO> marketCondition;
}
