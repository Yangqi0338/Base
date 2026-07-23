package com.newzkl.platform.base.biz.finance.model.assembler;

import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.req.EarningRecordReq;
import com.newzkl.platform.base.biz.finance.model.earnings.req.IncomeQuery;
import com.newzkl.platform.base.biz.finance.model.earnings.res.AppEarningRecordRes;
import com.newzkl.platform.base.biz.finance.model.earnings.vo.EarningRecordVO;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 分润记录/贡献转换器
 * @author kc
 */
@Mapper(componentModel = "spring", imports = BaseConvert.class)
public interface EarningRecordAssembler extends BaseAssembler<EarningRecordReq, EarningRecordVO> {

    @Mappings({
            @Mapping(target = "goodsInfo", ignore = true),
    })
    AppEarningRecordRes vo2AppVO(EarningRecordVO it);

    AccountPurseAlterRecordReq vo2Req(EarningRecordReq earningInfoVO);

    EarningRecordQuery incomeQuery2Query(IncomeQuery query);

}