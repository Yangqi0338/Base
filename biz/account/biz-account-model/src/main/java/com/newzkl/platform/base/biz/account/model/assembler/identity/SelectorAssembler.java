package com.newzkl.platform.base.biz.account.model.assembler.identity;


import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.req.SelectorEditReq;
import com.newzkl.platform.base.biz.account.model.res.SelectorOutRes;
import com.newzkl.platform.base.biz.account.model.vo.SelectorVO;
import org.mapstruct.Mapper;

/**
 * c端客户
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface SelectorAssembler extends BaseAssembler<SelectorEditReq, SelectorVO> {

    SelectorOutRes vo2OutRes(SelectorVO identityVO);
}
