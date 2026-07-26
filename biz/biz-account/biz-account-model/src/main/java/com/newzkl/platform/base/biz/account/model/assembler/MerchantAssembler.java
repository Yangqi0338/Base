package com.newzkl.platform.base.biz.account.model.assembler;

import com.newzkl.platform.base.biz.account.model.merchant.req.MerchantReq;
import com.newzkl.platform.base.biz.account.model.merchant.res.MerchantRes;
import com.newzkl.platform.base.biz.account.model.merchant.vo.MerchantVO;
import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import org.mapstruct.Mapper;

/**
 * 商户转换器。
 *
 * @author KC
 */
@Mapper(componentModel = "spring")
public interface MerchantAssembler extends BaseAssembler<MerchantReq, MerchantVO> {

    /**
     * 领域视图转对外出参。
     *
     * @param it 领域视图
     * @return 对外出参
     */
    MerchantRes vo2Res(MerchantVO it);
}
