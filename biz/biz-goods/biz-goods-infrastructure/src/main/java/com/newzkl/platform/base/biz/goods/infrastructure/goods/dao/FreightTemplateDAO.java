package com.newzkl.platform.base.biz.goods.infrastructure.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.FreightTemplateDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.freight.FreightTemplateQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.freight.FreightTemplateVO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 运费模板
 *
 * @author fang
 */
@Mapper
public interface FreightTemplateDAO extends BaseMapper<FreightTemplateDO> {

    default BaseLambdaQueryWrapper<FreightTemplateDO> getLw(FreightTemplateQuery freightTemplateQuery) {
        return new BaseLambdaQueryWrapper<FreightTemplateDO>()
                .notEmptyIn(FreightTemplateDO::getId, freightTemplateQuery.getIdList())
                .notEmptyIn(FreightTemplateDO::getAccountId, freightTemplateQuery.getAccountIdList())
                .notEmptyLike(FreightTemplateDO::getName, freightTemplateQuery.getName())
                ;
    };
}