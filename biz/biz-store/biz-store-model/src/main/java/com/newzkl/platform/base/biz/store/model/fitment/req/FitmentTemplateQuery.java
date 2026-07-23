package com.newzkl.platform.base.biz.store.model.fitment.req;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author niu
 * @description: 装修模板查询
 * @date 2024/3/29 15:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FitmentTemplateQuery extends PageQuery {

    /**
     * 运营商id
     */
    private Long operatorId;

    /**
     * 渠道id 0：平台配置  1：样板店快照  >1：渠道商装修
     */
    private Long channelId;

    /**
     * 门店id/样板店id
     */
    private Long shopId;

}
