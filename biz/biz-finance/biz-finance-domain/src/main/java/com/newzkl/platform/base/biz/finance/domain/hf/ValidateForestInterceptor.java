package com.newzkl.platform.base.biz.finance.domain.hf;

import cn.hutool.core.util.ArrayUtil;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.interceptor.Interceptor;
import com.newzkl.platform.base.common.core.model.check.CheckCommand;
import com.newzkl.platform.base.common.core.model.check.UpdateCommand;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;

/**
 * validation 拦截器
 */
@Slf4j
public abstract class ValidateForestInterceptor implements Interceptor<Void> {

    public <T> void validate(ForestRequest request, T obj) {
        String isModify = request.getHeaderValue("isModify");

        Class<?>[] checkGroupClazz = new Class[0];
        if (Boolean.TRUE.toString().equalsIgnoreCase(isModify)) {
            checkGroupClazz = ArrayUtil.append(checkGroupClazz, UpdateCommand.class);
        } else {
            checkGroupClazz = ArrayUtil.append(checkGroupClazz, Default.class, CheckCommand.class);
        }

        CommonUtil.validate(obj, checkGroupClazz);
    }
}

