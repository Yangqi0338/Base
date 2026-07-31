package com.newzkl.platform.base.biz.order.infrastructure.assembler;


import com.newzkl.platform.base.biz.order.infrastructure.entity.RefundDO;
import com.newzkl.platform.base.biz.order.model.dto.Refund;
import com.newzkl.platform.base.biz.order.model.vo.RefundExcelVO;
import com.newzkl.platform.base.biz.order.model.vo.RefundVO;
import org.mapstruct.Mapper;

/**
* 售后单
* @author fang
*/
@Mapper(componentModel = "spring", uses = RefundConvert.class)
public interface RefundAssembler {
    /**
     * DO转Domain
     * @param refundDO
     * @return
    */
    Refund doToDomain(RefundDO refundDO);
    /**
     * Domain转DO
     * @param refund
     * @return
     */
    RefundDO domainToDO(Refund refund);

    /**
     * DO转VO
     * @param refundDO
     * @return
     */
    RefundVO doToVO(RefundDO refundDO);

    /**
     * VO转DO
     * @param refundVO
     * @return
     */
    RefundDO voToDO(RefundVO refundVO);

    RefundExcelVO vo2ExcelVO(RefundVO vo);
}
