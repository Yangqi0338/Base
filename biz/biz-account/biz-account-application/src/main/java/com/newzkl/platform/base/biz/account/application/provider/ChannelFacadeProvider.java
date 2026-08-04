package com.newzkl.platform.base.biz.account.application.provider;

import cn.hutool.core.bean.BeanUtil;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.facade.ChannelFacade;
import com.newzkl.platform.base.biz.account.facade.model.ChannelOutVO;
import com.newzkl.platform.base.biz.account.facade.model.ChannelRpcQuery;
import com.newzkl.platform.base.biz.account.model.req.ChannelQuery;
import com.newzkl.platform.base.biz.account.model.vo.ChannelVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@code OperatorFacade} 的本域实现
 *
 * <p>对等旧 {@code com.zkl.scm.user.application.rpc.OperatorFacadeImpl} —— 旧版是
 * Dubbo provider (`@DubboService`), Base 当前为单体 (全域同一 Spring 上下文),
 * 故以普通 {@code @Service} 暴露, 调用方直接注入接口。将来拆服务时只需在此类加
 * provider 注解, 调用方零改动。</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelFacadeProvider implements ChannelFacade {

    private final ChannelClientDomain channelClientDomain;

    @Override
    public List<ChannelOutVO> channelList(ChannelRpcQuery query) {
        ChannelQuery channelQuery = BeanUtil.copyProperties(query, ChannelQuery.class);

        // TODO
//        if (req.getChannelId()!= null){
//            channelQuery.setUpOperatorId(req.getChannelId());
//        }
        List<ChannelVO> channelVOS = channelClientDomain.channelPageList(channelQuery).getRecords();
        return TransferUtils.transfers(channelVOS, ChannelOutVO::new);
    }
}
