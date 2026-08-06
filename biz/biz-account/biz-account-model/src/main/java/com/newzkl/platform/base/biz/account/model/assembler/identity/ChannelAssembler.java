package com.newzkl.platform.base.biz.account.model.assembler.identity;

import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelUpdateReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityAccountQuery;
import com.newzkl.platform.base.common.ddd.facade.ChannelOutRes;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 渠道商
 *
 * @author fang
 */
@Mapper(componentModel = "spring", uses = {BaseConvert.class})
public interface ChannelAssembler extends BaseAssembler<ChannelReq, ChannelVO> {

    ChannelOutRes vo2OutRes(ChannelVO identityVO);

    @Mappings({
            @Mapping(target = "companyInfo", source = "companyInfo", qualifiedByName = "toJson")
    })
    ChannelVO updateReq2VO(ChannelUpdateReq channelUpdateReq);

    IdentityAccountQuery query2IdentityQuery(ChannelQuery channelQuery);

}
