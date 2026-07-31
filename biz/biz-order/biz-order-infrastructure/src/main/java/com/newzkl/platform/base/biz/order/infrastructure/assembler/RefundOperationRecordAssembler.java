package com.newzkl.platform.base.biz.order.infrastructure.assembler;


import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundOperationRecordDO;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordEntity;
import org.mapstruct.Mapper;

/**
 * 售后操作记录 DO/Entity 转换器（MapStruct）
 *
 * @author 开发者名称
 * @since 2026-01-23
 */
@Mapper(componentModel = "spring", uses = RefundOperationRecordConvert.class)
public interface RefundOperationRecordAssembler {

    /**
     * DO转领域实体
     * @param recordDO 数据库实体
     * @return 领域实体
     */
    RefundOperationRecordEntity doToDomain(RefundOperationRecordDO recordDO);

    /**
     * 领域实体转DO
     * @param entity 领域实体
     * @return 数据库实体
     */
    RefundOperationRecordDO domainToDO(RefundOperationRecordEntity entity);
}