package com.newzkl.platform.base.biz.finance.model.assembler;

import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseReq;
import com.newzkl.platform.base.biz.finance.model.purse.req.AddAccountPurseReq;
import com.newzkl.platform.base.biz.finance.model.purse.res.BatchQueryAccountPurseRes;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 银行转换器
 *
 * @author kc
 */
@Mapper(componentModel = "spring", uses = BaseConvert.class)
public interface AccountPurseAssembler extends BaseAssembler<AccountPurseReq, AccountPurseVO> {

    List<AccountPurseVO> addReq2VOList(List<AddAccountPurseReq> req);

    @Mappings({
            @Mapping(target = "earnings", source = "initAmount"),
            @Mapping(target = "totalEarnings", source = "initAmount"),
    })
    AccountPurseVO addReq2VO(AddAccountPurseReq req);

    List<BatchQueryAccountPurseRes> do2BatchResList(List<AccountPurseVO> entityList);
}