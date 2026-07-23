package com.newzkl.platform.base.biz.goods.domain.virtualSpu.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.virtualSpu.CdkQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.virtualSpu.BuyCreateCdkReq;
import com.newzkl.platform.base.biz.goods.model.goods.req.virtualSpu.ToCdkCommand;
import com.newzkl.platform.base.biz.goods.model.goods.vo.virtualSpu.CdkVO;

import java.util.List;

/**
 * 兑换码
 *
 * @author fang
 */
public interface ICdkDomain {

    /**
     * 随机生成CDK
     */
    List<String> randomCreateCdk(Long belowId, Integer number, Integer systemType);

    /**
     * 分配CDK
     */
    int toCdk(ToCdkCommand toCdkCommand);

    void cdkStateEdit(Long id, Integer useState);

    /**
     * 购买生成兑换码
     */
    void buyCreateCdk(BuyCreateCdkReq buyCreateCdkReq);

    Page<CdkVO> cdkPage(CdkQuery cdkQuery);
}
