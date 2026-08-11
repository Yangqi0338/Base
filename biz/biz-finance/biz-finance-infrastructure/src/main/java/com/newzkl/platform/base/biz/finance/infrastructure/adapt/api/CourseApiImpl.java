package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.course.facade.CoursePurchaseFacade;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.CourseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseApiImpl implements CourseApi {

    @Autowired
    private CoursePurchaseFacade coursePurchaseFacade;

    @Override
    public void paySuccess(Long orderNo, String thirdOrderNo) {
        coursePurchaseFacade.paySuccess(orderNo,thirdOrderNo);
    }
}
