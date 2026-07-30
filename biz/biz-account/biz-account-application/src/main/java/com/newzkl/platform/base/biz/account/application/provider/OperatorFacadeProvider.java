package com.newzkl.platform.base.biz.account.application.provider;

import com.newzkl.platform.base.biz.account.domain.service.OperatorClientDomain;
import com.newzkl.platform.base.biz.account.facade.OperatorFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
public class OperatorFacadeProvider implements OperatorFacade {

    private final OperatorClientDomain operatorClientDomain;

    /**
     * 按运营类型查运营商可见的供应商 ID 列表
     *
     * @param accountId  运营商账号 ID
     * @param searchType 查询用的运营类型, 为 null 时取该运营商自身类型
     * @return 可见供应商 ID 列表; 账号 ID 为空或运营商不存在时返回空列表
     */
    @Override
    public List<Long> supplierIdListByType(Long accountId, Integer searchType) {
        if (accountId == null) {
            return Collections.emptyList();
        }
        return operatorClientDomain.supplierIdListByType(accountId, searchType);
    }
}
