package com.newzkl.platform.base.biz.finance.model.assembler;

import com.newzkl.platform.base.biz.finance.model.purse.req.BankReq;
import com.newzkl.platform.base.biz.finance.model.purse.vo.BankVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;

/**
 * 银行转换器
 *
 * @author kc
 */
@Mapper(componentModel = "spring", uses = BaseConvert.class)
public interface BankAssembler extends BaseAssembler<BankReq, BankVO> {

}