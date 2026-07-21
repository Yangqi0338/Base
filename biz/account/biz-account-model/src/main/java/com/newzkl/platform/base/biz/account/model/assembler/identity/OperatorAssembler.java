package com.newzkl.platform.base.biz.account.model.assembler.identity;

import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.req.OperatorReq;
import com.newzkl.platform.base.biz.account.model.res.OperatorOutRes;
import com.newzkl.platform.base.biz.account.model.vo.OperatorVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 市场运营商
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface OperatorAssembler extends BaseAssembler<OperatorReq, OperatorVO> {

    OperatorOutRes vo2OutRes(OperatorVO identityVO);

    @Override
    @Mappings({
            @Mapping(target = "serviceFeeConfigVO", ignore = true)
    })
    OperatorVO req2VO(OperatorReq req);
}
