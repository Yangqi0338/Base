package com.newzkl.platform.base.biz.account.model.assembler.identity;

import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.dto.EmpDTO;
import com.newzkl.platform.base.biz.account.model.res.EmpOutRes;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;
import com.newzkl.platform.base.biz.account.model.vo.EmpVO;
import org.mapstruct.Mapper;

/**
 * 员工
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface EmpAssembler extends BaseAssembler<EmpOutRes, EmpVO> {

    EmpOutRes vo2OutRes(EmpVO identityVO);

    /**
     * 员工视图转纯净 DTO
     *
     * @param empVO 员工视图
     * @return 只含 emp 自有列的纯净 DTO
     * @ext 主数据 emp (无副数据)
     */
    EmpDTO vo2DTO(EmpVO empVO);

    /**
     * 员工视图转聚合出参
     *
     * <p>只搬主数据 emp 侧列(仅 {@code type}), 副数据 account 由 domain 显式填充</p>
     *
     * @param empVO 员工视图
     * @return 员工聚合出参 (副数据尚未填充)
     * @ext 主数据 emp, 副数据 account
     */
    EmpRes vo2Res(EmpVO empVO);
}
