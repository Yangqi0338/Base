package com.newzkl.platform.base.biz.store.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.store.infrastructure.entity.StoreAccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StoreAccountDAO extends BaseMapper<StoreAccountDO> {

}
