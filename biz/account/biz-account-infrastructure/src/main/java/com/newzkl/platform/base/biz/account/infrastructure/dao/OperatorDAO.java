package com.newzkl.platform.base.biz.account.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.account.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.biz.account.infrastructure.entity.OperatorDO;
import com.newzkl.platform.base.biz.account.model.req.OperatorQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 市场运营商
 *
 * @author fang
 */
@Mapper
public interface OperatorDAO extends BaseMapper<OperatorDO> {

    default BaseLambdaQueryWrapper<OperatorDO> getLw(OperatorQuery query) {
        return new BaseLambdaQueryWrapper<OperatorDO>()
                .notEmptyIn(OperatorDO::getId, query.getIdList())
                .notEmptyEq(OperatorDO::getType, query.getType())
                .notEmptyIn(OperatorDO::getTypeForeignId, query.getTypeForeignIdList())
                .notEmptyNotIn(OperatorDO::getTypeForeignId, query.getNotTypeForeignIdList())
                .notEmptyLike(OperatorDO::getDomain, query.getDomain())
                ;
    }

    List<Map<String, String>> groupCountByQuery(@Param("query") OperatorQuery query);

}