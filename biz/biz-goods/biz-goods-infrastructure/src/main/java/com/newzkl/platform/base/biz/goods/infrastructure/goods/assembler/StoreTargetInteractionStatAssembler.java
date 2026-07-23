package com.newzkl.platform.base.biz.goods.infrastructure.goods.assembler;

import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.StoreTargetInteractionStatDO;
import com.newzkl.platform.base.biz.goods.model.goods.entity.interaction.StoreTargetInteractionStat;
import com.newzkl.platform.base.biz.goods.model.goods.req.interaction.BatchSyncStatReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.interaction.InteractionStatRes;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 统计数据转换器（MapStruct 实现 VO ↔ 领域模型 ↔ DO 转换）
 * 
 * @author sijiwang
 */
@Mapper(componentModel = "spring", uses = BaseConvert.class) // 依赖辅助转换类
public interface StoreTargetInteractionStatAssembler {
    // 单例实例（非 Spring 环境可用）
    StoreTargetInteractionStatAssembler INSTANCE = Mappers.getMapper(StoreTargetInteractionStatAssembler.class);
    
    /** VO → 领域模型 */
    @Mappings({@Mapping(source = "extMap", target = "extMap")})
    StoreTargetInteractionStat toDomain(BatchSyncStatReq requestVO);
    
    /** 领域模型 → DO（核心：处理 extMap ↔ extJson 转换） */
    @Mappings({@Mapping(source = "extMap", target = "extJson", qualifiedByName = "mapToJson")})
    StoreTargetInteractionStatDO toDO(StoreTargetInteractionStat domainModel);
    
    /** DO → 领域模型（核心：处理 extJson ↔ extMap 转换） */
    @Mappings({ @Mapping(source = "extJson", target = "extMap", qualifiedByName = "jsonToMap")})
    StoreTargetInteractionStat toDomain(StoreTargetInteractionStatDO doEntity);
    
    /** 领域模型 → 响应 VO */
    @Mappings({@Mapping(source = "extMap", target = "extMap")})
    InteractionStatRes toResponseVO(StoreTargetInteractionStat domainModel);
    
    /** 批量：VO 列表 → 领域模型列表 */
    List<StoreTargetInteractionStat> toDomainList(List<BatchSyncStatReq> requestVOList);
    
    /** 批量：领域模型列表 → DO 列表 */
    List<StoreTargetInteractionStatDO> toDOList(List<StoreTargetInteractionStat> domainModelList);
    
    /** 批量：DO 列表 → 领域模型列表 */
    List<StoreTargetInteractionStat> toDomainListFromDO(List<StoreTargetInteractionStatDO> doEntityList);
    
    /** 批量：领域模型列表 → 响应 VO 列表 */
    List<InteractionStatRes> toResponseVOList(List<StoreTargetInteractionStat> domainModelList);
}