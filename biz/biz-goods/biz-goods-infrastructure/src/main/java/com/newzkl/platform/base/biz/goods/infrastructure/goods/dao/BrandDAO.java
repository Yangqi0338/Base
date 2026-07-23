package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.BrandDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.brand.BrandPageQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.BrandVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌
 *
 * @author fang
 */
@Mapper
public interface BrandDAO extends BaseMapper<BrandDO> {

    Page<BrandVO> queryPage(Page<?> page, @Param("query") BrandPageQuery brandQuery);

}