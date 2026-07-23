package com.newzkl.platform.base.biz.finance.model.assembler;


import com.newzkl.platform.base.biz.finance.model.account.req.BillOrderAwardReq;
import com.newzkl.platform.base.biz.finance.model.account.vo.BillOrderAwardVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;

/**
 * 银行转换器
 *
 * @author kc
 */
@Mapper(componentModel = "spring", uses = BaseConvert.class)
public interface BillOrderAwardAssembler extends BaseAssembler<BillOrderAwardReq, BillOrderAwardVO> {
}