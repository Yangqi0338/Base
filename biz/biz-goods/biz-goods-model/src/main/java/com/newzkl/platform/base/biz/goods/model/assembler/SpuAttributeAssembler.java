package com.newzkl.platform.base.biz.goods.model.assembler;

import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuAttributeDTO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * spu 属性装配器
 *
 * <p>{@code SpuAttributeDO.type} 为枚举, 由 DO → DTO 一跳按 {@code getCode()} 落成 Integer,
 * 本装配器仅做 DTO → VO 同名同类型直传</p>
 *
 * @author KC
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SpuAttributeAssembler extends BaseAssembler<SpuAttributeDTO, SpuAttributeVO> {
}
