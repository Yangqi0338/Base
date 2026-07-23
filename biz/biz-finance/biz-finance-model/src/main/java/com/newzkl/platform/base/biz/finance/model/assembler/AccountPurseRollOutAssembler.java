package com.newzkl.platform.base.biz.finance.model.assembler;

import com.newzkl.platform.base.biz.finance.model.purse.req.RollOutApplyReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.web.RollOutApplyRequest;
import com.newzkl.platform.base.biz.finance.model.purse.vo.RollOutApplyVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;

/**
 * 银行转换器
 *
 * @author kc
 */
@Mapper(componentModel = "spring", imports = BaseConvert.class)
public interface AccountPurseRollOutAssembler extends BaseAssembler<RollOutApplyReq, RollOutApplyVO> {

    RollOutApplyReq applyRequest2Req(RollOutApplyRequest req);
}