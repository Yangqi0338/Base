package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.IndustryDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.IndustryPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.IndustryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 行业
 *
 * @author fang
 */
@Mapper
@Repository
public interface IndustryDAO extends BaseMapper<IndustryDO> {

    Page<IndustryVO> queryPage(Page<?> page, @Param("query") IndustryPageQuery industryQuery);

}