package com.newzkl.platform.base.biz.account.model.assembler.identity;

import com.newzkl.platform.base.common.ddd.model.BaseAssembler;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import com.newzkl.platform.base.biz.account.model.dto.ChannelDTO;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.biz.account.model.req.IdentityAccountQuery;
import com.newzkl.platform.base.biz.account.model.res.ChannelRes;
import com.newzkl.platform.base.common.ddd.facade.ChannelOutRes;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 渠道商
 *
 * @author fang
 */
@Mapper(componentModel = "spring", uses = {BaseConvert.class})
public interface ChannelAssembler extends BaseAssembler<ChannelReq, ChannelVO> {

    ChannelOutRes vo2OutRes(ChannelVO identityVO);

    IdentityAccountQuery query2IdentityQuery(ChannelQuery channelQuery);

    /**
     * 渠道商视图转纯净 DTO
     *
     * @param channelVO 渠道商视图
     * @return 只含 channel 自有列的纯净 DTO
     * @ext 主数据 channel (无副数据)
     */
    ChannelDTO vo2DTO(ChannelVO channelVO);

    /**
     * 渠道商视图转聚合出参
     *
     * <p>只搬主数据 channel 侧列, 副数据 account 由 domain 显式填充。
     * {@code tripartiteAccountPermission} 两侧类型不可转 (String → 枚举) 且真值在 account 侧, 故在此忽略</p>
     *
     * @param channelVO 渠道商视图
     * @return 渠道商聚合出参 (副数据尚未填充)
     * @ext 主数据 channel, 副数据 account
     */
    @Mapping(target = "tripartiteAccountPermission", ignore = true)
    ChannelRes vo2Res(ChannelVO channelVO);

}
