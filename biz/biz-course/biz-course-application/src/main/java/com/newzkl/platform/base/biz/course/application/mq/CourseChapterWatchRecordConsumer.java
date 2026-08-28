package com.newzkl.platform.base.biz.course.application.mq;

import com.newzkl.platform.base.biz.course.domain.service.CourseChapterWatchRecordDomain;
import com.newzkl.platform.base.biz.course.domain.service.CourseDomain;
import com.newzkl.platform.base.biz.course.model.event.CourseChapterWatchRecordAddReq;
import com.newzkl.platform.base.biz.course.model.watch.req.CourseChapterWatchRecordReq;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;


import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 用户课程章节观看记录
 * @author sijiwang
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.COURSE_CHAPTER_WATCH_RECORD_MESSAGE, tag = MQ.Tag.COURSE_CHAPTER_WATCH_RECORD_EVENT)
public class CourseChapterWatchRecordConsumer extends AbstractMessageMQPushConsumer<CourseChapterWatchRecordAddReq> {

    @Autowired
    private CourseChapterWatchRecordDomain courseDomain;
    @Override
    public void remoteProcess(CourseChapterWatchRecordAddReq message, Map<String, Object> extMap) {
        CourseChapterWatchRecordReq recordReq = TransferUtils.transfer(message, CourseChapterWatchRecordReq.class);
        courseDomain.saveOrUpdate(recordReq);
    }
}
