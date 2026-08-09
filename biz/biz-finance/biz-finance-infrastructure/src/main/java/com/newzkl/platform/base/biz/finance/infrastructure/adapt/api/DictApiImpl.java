package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.DictApi;
import com.newzkl.platform.base.biz.sys.facade.IDictFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * {@code DictApi} 的跨域实现
 *
 * <p>经 {@link IDictFacade} 调 biz-sys 字典能力。对等旧
 * {@code @DubboReference IDictFacade}: Base 当前为单体, facade 实现与本类同上下文,
 * 直接按接口注入即可; 将来拆服务时改为远程 consumer, 本类与领域层零改动。</p>
 *
 * <p>字典值为 JSON 字符串, 反序列化 / null 兜底由调用方仓储负责
 * (见 {@code AccountPurseConfigRepositoryImpl}/{@code WithdrawRepositoryImpl})。</p>
 *
 * @author KC
 */
@Component("financeDictApiImpl")
@RequiredArgsConstructor
public class DictApiImpl implements DictApi {

    private final IDictFacade dictFacade;

    @Override
    public String get(Long code) {
        return dictFacade.get(code);
    }

    @Override
    public void set(Long code, String value) {
        dictFacade.set(code, value);
    }
}
