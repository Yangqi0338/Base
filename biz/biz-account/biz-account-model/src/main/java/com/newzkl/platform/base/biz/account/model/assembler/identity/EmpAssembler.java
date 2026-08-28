package com.newzkl.platform.base.biz.account.model.assembler.identity;

import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
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

    EmpRes vo2Res(EmpVO empVO);
}
