package com.newzkl.platform.base.biz.goods.domain.brand.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.IndustryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.brand.IndustryReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.IndustryVO;

import java.util.List;

/**
* 行业
* @author fang
*/
public interface IIndustryRepository {

    void industrySave(IndustryReq req);

    void industryDelete(List<Long> idList);

    void industryEdit(IndustryReq req);

    IndustryVO industry(Long id);

    List<String> categoryIdListById(List<Long> industryIdList);

    Page<IndustryVO> queryPage(IndustryPageQuery industryQuery);

    void buildPageQuery(IndustryPageQuery industryQuery);

}
