package com.newzkl.platform.base.common.core.mq.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.common.core.mq.infrastructure.entity.LocalMessageDO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 本地消息 DAO
 *
 * @author fang
 */
@Mapper
public interface LocalMessageDAO extends BaseMapper<LocalMessageDO> {

    /**
     * 构建按发送状态的查询条件
     *
     * @param sendState 发送状态
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<LocalMessageDO> getLw(MQEnum.SendState sendState) {
        BaseLambdaQueryWrapper<LocalMessageDO> lw = new BaseLambdaQueryWrapper<LocalMessageDO>()
                .notEmptyEq(LocalMessageDO::getSendState, sendState);
        lw.orderByAsc(LocalMessageDO::getId);
        return lw;
    }
}
