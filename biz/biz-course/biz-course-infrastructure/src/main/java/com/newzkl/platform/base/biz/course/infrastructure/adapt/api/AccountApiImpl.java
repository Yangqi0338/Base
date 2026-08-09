package com.newzkl.platform.base.biz.course.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.course.domain.adapt.api.AccountApi;
import org.springframework.stereotype.Component;

@Component("courseAccountApi")
public class AccountApiImpl implements AccountApi {
    @Override
    public Long currentUserId() {
        return 0L;
    }

    @Override
    public boolean existsUser(Long userId) {
        return false;
    }
}
