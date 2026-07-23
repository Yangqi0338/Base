package com.newzkl.platform.base.biz.finance.model.assembler;

import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.vo.PurchaseRecordVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;

/**
 * 银行转换器
 *
 * @author kc
 */
@Mapper(componentModel = "spring", imports = BaseConvert.class)
public interface PurchaseRecordAssembler extends BaseAssembler<PurchaseRecordReq, PurchaseRecordVO> {


}