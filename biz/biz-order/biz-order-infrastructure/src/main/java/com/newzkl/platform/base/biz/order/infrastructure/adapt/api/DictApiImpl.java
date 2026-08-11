package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.account.facade.OperatorFacade;
import com.newzkl.platform.base.biz.order.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.OperatorApi;
import com.newzkl.platform.base.common.ddd.facade.OrderConfigVO;
import com.newzkl.platform.base.biz.sys.facade.IDictFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@code OperatorApi} 的跨域实现
 *
 * <p>经 {@code OperatorFacade} 调 biz-account 的运营商能力。对等旧
 * {@code @DubboReference IOperatorFacade}: Base 当前为单体, facade 实现
 * ({@code OperatorFacadeProvider}) 与本类同上下文, 直接按接口注入即可;
 * 将来拆服务时改为远程 consumer, 本类与领域层零改动。</p>
 *
 * @author KC
 */
@Slf4j
@Component("orderDictApi")
@RequiredArgsConstructor
public class DictApiImpl implements DictApi {

    private final IDictFacade dictFacade;

    @Override
    public OrderConfigVO get(Long id) {
        String value = dictFacade.get(id);
        if(StrUtil.isEmpty(value)){
            OrderConfigVO orderConfigVO = new OrderConfigVO();
            orderConfigVO.setAutoReceive(7);
            orderConfigVO.setNotRefund(7);
            orderConfigVO.setAutoAgreeRefund(7);
            return orderConfigVO;
        }else{
            return JSONObject.parseObject(value, OrderConfigVO.class);
        }
    }

    @Override
    public void set(Long id, String value) {
        dictFacade.set(id, value);
    }
}
