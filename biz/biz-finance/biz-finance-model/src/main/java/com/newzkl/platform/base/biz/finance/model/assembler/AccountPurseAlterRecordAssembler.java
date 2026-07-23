package com.newzkl.platform.base.biz.finance.model.assembler;

import com.newzkl.platform.base.biz.finance.model.purse.req.*;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 银行转换器
 *
 * @author kc
 */
@Mapper(componentModel = "spring", imports = BaseConvert.class)
public interface AccountPurseAlterRecordAssembler extends BaseAssembler<AccountPurseAlterRecordReq, AccountPurseAlterRecordVO> {

    @Mappings({
            @Mapping(target = "amount", source = "payAmount"),
            @Mapping(target = "joinRecordId", source = "orderNo"),
    })
    AccountPurseAlterRecordReq payReq2Req(BalancePayReq payReq);

    @Mappings({
            @Mapping(target = "accountId", source = "supplierId"),
    })
    AccountPurseAlterRecordReq goodsSeatReq2Req(SupplierPurchaseGoodsSeatReq goodsSeatReq);

    @Mappings({
            @Mapping(target = "amount", source = "refundAmount"),
            @Mapping(target = "joinRecordId", source = "sellAfterOrderNo"),
    })
    AccountPurseAlterRecordReq refundReq2Req(SellAfterRefundReq refundReq);

    AccountPurseAlterRecordReq settleReq2Req(SupplierSettleReq settleReq);
}