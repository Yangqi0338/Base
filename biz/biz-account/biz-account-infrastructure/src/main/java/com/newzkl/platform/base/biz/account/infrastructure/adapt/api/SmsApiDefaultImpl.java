package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.SmsApi;
import com.newzkl.platform.base.biz.account.model.support.CodeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * {@link SmsApi} 默认兜底实现。
 *
 * <p>TODO[cross-service]: 短信通道为独立能力, 跨域 provider 链已整体延迟。
 * 入口 starter 侧应以真实短信实现覆盖此默认实现。</p>
 *
 * @author KC
 */
@Slf4j
@Component
public class SmsApiDefaultImpl implements SmsApi {

    @Override
    public void sendCode(CodeReq codeReq) {
        // TODO[cross-service]: 远程短信发送, 默认仅记录日志
        log.warn("[SmsApiDefaultImpl] 短信未真实发送, req={}", codeReq);
    }
}
