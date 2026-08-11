package com.newzkl.platform.base.common.ddd.infrastructure.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.IdGeneratorDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * id_generator 号段表 Mapper
 *
 * @author KC
 */
@Mapper
public interface IdGeneratorMapper extends BaseMapper<IdGeneratorDO> {
}
