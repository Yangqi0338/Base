package com.newzkl.platform.base.biz.goods.domain.brand.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.IndustryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.IndustryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.IndustryVO;

import java.util.List;

/**
 * 品牌
 *
 * @author fang
 */
public interface IIndustryDomain {
    /**
     * 行业保存
     *
     * @param industryReq
     * @return
     */
    Long industrySave(IndustryReq industryReq);

    /**
     * 行业详情
     *
     * @param id
     * @return
     */
    IndustryVO industry(Long id);

    /**
     * 行业删除
     *
     * @param idList
     */
    void industryDelete(List<Long> idList);

    /**
     * 行业分页
     */
    Page<IndustryVO> industryPage(IndustryPageQuery industryQuery);

}
