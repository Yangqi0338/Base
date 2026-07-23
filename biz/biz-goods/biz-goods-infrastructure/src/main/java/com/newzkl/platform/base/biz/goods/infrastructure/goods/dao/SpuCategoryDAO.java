package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.SpuCategoryDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类
 *
 * @author fang
 */
@Mapper
public interface SpuCategoryDAO extends BaseMapper<SpuCategoryDO> {

    default BaseLambdaQueryWrapper<SpuCategoryDO> getLw(SpuCategoryQuery query) {
        return new BaseLambdaQueryWrapper<SpuCategoryDO>()
                .notEmptyIn(SpuCategoryDO::getId, query.getIdList())
                .likeList(SpuCategoryDO::getPid, CollUtil.map(query.getPidList(), id -> id + ",", true))
                .notEmptyEq(SpuCategoryDO::getPid, query.getPid())
                .notEmptyLike(SpuCategoryDO::getName, query.getName())
                .notEmptyEq(SpuCategoryDO::getName, query.getNameEq())
                .notNullNe(SpuCategoryDO::getId, query.getNotId())
                .notEmptyIn(SpuCategoryDO::getAccountId, query.getAccountIdList())
                ;
    }

    Integer categoryCount(@Param("query") SpuCategoryQuery categoryQuery);

    List<SpuCategoryVO> listByQuery(Page<?> page, @Param("query") SpuCategoryQuery categoryQuery);

}
