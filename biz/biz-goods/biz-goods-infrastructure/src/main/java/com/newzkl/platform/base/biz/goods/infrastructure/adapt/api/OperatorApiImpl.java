package com.newzkl.platform.base.biz.goods.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.facade.OperatorFacade;
import com.newzkl.platform.base.biz.goods.domain.adapt.api.OperatorApi;
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
@Component("goodsOperatorApi")
@RequiredArgsConstructor
public class OperatorApiImpl implements OperatorApi {

    private final OperatorFacade operatorFacade;

    /**
     * 按运营类型查运营商可见的供应商 ID 列表
     *
     * @param accountId  运营商账号 ID
     * @param searchType 查询用的运营类型, 为 null 时取该运营商自身类型
     * @return 可见供应商 ID 列表; 运营商不存在时返回空列表
     */
    @Override
    public List<Long> supplierIdListByType(Long accountId, Integer searchType) {
        return operatorFacade.supplierIdListByType(accountId, searchType);
    }
}
