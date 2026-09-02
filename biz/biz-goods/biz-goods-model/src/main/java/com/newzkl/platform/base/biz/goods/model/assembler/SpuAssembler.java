package com.newzkl.platform.base.biz.goods.model.assembler;

import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * spu 装配器
 *
 * <p>{@code SpuDO} 由 {@code TransferUtils.transfer(do, SpuDTO.class)} 转入(枚举字段两侧同类型,
 * 属引用直传), 再经本装配器出 {@code SpuVO}, 枚举按 {@code getCode()} 落成 Integer。
 * 原实现直接 {@code BeanUtil} 拷 DO → VO, 枚举按 <b>ordinal</b> 转 Integer,
 * 如 {@code State.SALE}(code 2, ordinal 3) 会错输出 3, 两跳后修正。</p>
 *
 * @author KC
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {SkuAssembler.class})
public interface SpuAssembler extends BaseAssembler<SpuDTO, SpuVO> {

    /**
     * spu 状态枚举转对外码值
     *
     * @param state 状态枚举
     * @return 码值, 入参为 null 时返回 null
     */
    default Integer stateToCode(SpuEnum.State state) {
        return state == null ? null : state.getCode();
    }

    /**
     * 发货时效类型枚举转对外码值
     *
     * @param deliverTimeType 发货时效类型枚举
     * @return 码值, 入参为 null 时返回 null
     */
    default Integer deliverTimeTypeToCode(SpuEnum.DeliverTimeType deliverTimeType) {
        return deliverTimeType == null ? null : deliverTimeType.getCode();
    }

    /**
     * 审核状态枚举转对外码值
     *
     * @param auditState 审核状态枚举
     * @return 码值, 入参为 null 时返回 null
     */
    default Integer auditStateToCode(AuditEnum.State auditState) {
        return auditState == null ? null : auditState.getCode();
    }
}
