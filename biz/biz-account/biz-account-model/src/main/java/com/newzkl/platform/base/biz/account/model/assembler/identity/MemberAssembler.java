package com.newzkl.platform.base.biz.account.model.assembler.identity;


import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.biz.account.model.req.MemberLoginOrRegisterReq;
import com.newzkl.platform.base.biz.account.model.res.MemberOutRes;
import com.newzkl.platform.base.biz.account.model.vo.MemberVO;
import org.mapstruct.Mapper;

/**
 * c端客户
 *
 * @author fang
 */
@Mapper(componentModel = "spring")
public interface MemberAssembler extends BaseAssembler<MemberLoginOrRegisterReq, MemberVO> {
    MemberOutRes vo2OutRes(MemberVO identityVO);
}
