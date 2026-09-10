package com.newzkl.platform.base.common.core.mq.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.common.core.mq.infrastructure.entity.LocalMessageDO;
import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;

import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
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
    default BaseLambdaQueryWrapper<LocalMessageDO> getLw(LocalMessageDTO dto) {
        return new BaseLambdaQueryWrapper<LocalMessageDO>()
                .notEmptyEq(LocalMessageDO::getSendState, dto.getSendState())
                .notEmptyEq(LocalMessageDO::getCanConsume, dto.getCanConsume())
                .notEmptyEq(LocalMessageDO::getConsumeState, dto.getConsumeState())
                .notEmptyLt(LocalMessageDO::getConsumeTime, dto.getConsumeTime())
                .notEmptyEq(LocalMessageDO::getOutKey, dto.getOutKey())
                .notEmptyEq(LocalMessageDO::getTopic, dto.getTopic())
                .notEmptyLike(LocalMessageDO::getTag, dto.getTag())
                .notEmptyLt(LocalMessageDO::getSendTime, dto.getSendTime());
    }
}
