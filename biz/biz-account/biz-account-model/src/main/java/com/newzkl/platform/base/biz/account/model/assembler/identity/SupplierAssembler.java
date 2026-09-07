package com.newzkl.platform.base.biz.account.model.assembler.identity;

import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.dto.SupplierDTO;
import com.newzkl.platform.base.biz.account.model.req.SupplierReq;
import com.newzkl.platform.base.biz.account.model.res.SupplierRes;
import com.newzkl.platform.base.biz.account.model.vo.SupplierAccountVO;
import com.newzkl.platform.base.biz.account.model.vo.SupplierVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 供应商装配器
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface SupplierAssembler extends BaseAssembler<SupplierReq, SupplierVO> {

    @Override
    @Mappings({
            @Mapping(target = "receiveAddress", ignore = true)
    })
    SupplierVO req2VO(SupplierReq req);

    /**
     * 供应商联表视图转分页出参
     *
     * <p>联表只带 {@code username} / {@code realName} 两个 account 列, 其余 account 字段留空,
     * 分页场景不逐行补</p>
     *
     * @param it 供应商联表视图
     * @return 供应商分页出参
     * @ext 主数据 supplier, 副数据 account(仅联表两列)
     */
    SupplierRes accountVO2Res(SupplierAccountVO it);

    /**
     * 供应商视图转纯净 DTO
     *
     * @param supplierVO 供应商视图
     * @return 只含 supplier 自有列的纯净 DTO
     * @ext 主数据 supplier (无副数据)
     */
    SupplierDTO vo2DTO(SupplierVO supplierVO);

    /**
     * 供应商视图转聚合出参
     *
     * <p>只搬主数据 supplier 侧列, 副数据 account 由 domain 显式填充</p>
     *
     * @param supplierVO 供应商视图
     * @return 供应商聚合出参 (副数据尚未填充)
     * @ext 主数据 supplier, 副数据 account
     */
    SupplierRes vo2Res(SupplierVO supplierVO);
}
