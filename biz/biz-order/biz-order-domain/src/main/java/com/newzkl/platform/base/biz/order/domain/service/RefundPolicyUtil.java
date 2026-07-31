package com.newzkl.platform.base.biz.order.domain.service;


import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/1418:30
 */
public class RefundPolicyUtil {

    public static RefundEnum.State getRefundInitState(RoleEnum.CompanyRole createRole, SpuEnum.ChannelType spuBelowType) {
        //客户申请
        if(RoleEnum.CompanyRole.MEMBER == createRole){
            //自营
            if(SpuEnum.ChannelType.CUSTOM == spuBelowType){
                return RefundEnum.State.CHANNEL_WAIT;
            //选品
            }else if(SpuEnum.ChannelType.SELECTION == spuBelowType){
                return RefundEnum.State.CHANNEL_WAIT;
            }
            //选品
            else if(SpuEnum.ChannelType.OUT == spuBelowType){
                return RefundEnum.State.CHANNEL_WAIT;
            }else {
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
        //渠道商申请
        }else if(RoleEnum.CompanyRole.CHANNEL == createRole) {
            //自营
            if(SpuEnum.ChannelType.CUSTOM == spuBelowType){
                ThrowsException.exception(BaseErrorCode.PARAM);
            //选品
            }else if(SpuEnum.ChannelType.SELECTION == spuBelowType){
                return RefundEnum.State.SUPPLIER_WAIT;
            }else if(SpuEnum.ChannelType.OUT == spuBelowType){
                return RefundEnum.State.SUPPLIER_WAIT;
            }else {
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
        }else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }
        return null;
    }
}
