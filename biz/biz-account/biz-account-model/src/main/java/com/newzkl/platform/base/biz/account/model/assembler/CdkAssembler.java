package com.newzkl.platform.base.biz.account.model.assembler;

import com.newzkl.platform.base.biz.account.model.cdk.req.CdkReq;
import com.newzkl.platform.base.biz.account.model.cdk.res.CdkRes;
import com.newzkl.platform.base.biz.account.model.cdk.vo.CdkVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import org.mapstruct.Mapper;

/**
 * 开通码转换器
 *
 * @author KC
 */
@Mapper(componentModel = "spring")
public interface CdkAssembler extends BaseAssembler<CdkReq, CdkVO> {

    /**
     * 领域视图转对外出参
     *
     * @param it 领域视图
     * @return 对外出参
     */
    CdkRes vo2Res(CdkVO it);
}
