package com.newzkl.platform.base.biz.account.model.assembler.identity;

import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.req.DealerEditReq;
import com.newzkl.platform.base.biz.account.model.res.DealerOutRes;
import com.newzkl.platform.base.biz.account.model.vo.DealerVO;
import org.mapstruct.Mapper;

/**
 * 市场交易师
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface DealerAssembler extends BaseAssembler<DealerEditReq, DealerVO> {

    DealerOutRes vo2OutRes(DealerVO identityVO);
}
