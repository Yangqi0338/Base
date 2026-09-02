package com.newzkl.platform.base.biz.goods.model.assembler;

import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * sku 装配器
 *
 * <p>{@code SkuDO} 由 {@code TransferUtils.transfer(do, SkuDTO.class)} 转入,
 * 再经本装配器出 {@code SkuVO}: Assembler 位于 model 层取不到 infrastructure 的 DO 类,
 * 故走 DO → DTO → VO 两跳</p>
 *
 * @author KC
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SkuAssembler extends BaseAssembler<SkuDTO, SkuVO> {
}
