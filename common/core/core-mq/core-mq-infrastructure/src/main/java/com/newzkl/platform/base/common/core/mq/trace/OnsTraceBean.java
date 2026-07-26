package com.newzkl.platform.base.common.core.mq.trace;

import lombok.Data;

/**
 * Created by alvin on 16-3-9.
 */
@Data
public class OnsTraceBean {
    private static String LOCAL_ADDRESS;
    private String topic = "";
    private String msgId = "";
    private String offsetMsgId = "";
    private String tags = "";
    private String keys = "";
    private String storeHost;
    private String clientHost;
    private long storeTime;
    private int retryTimes;
    private int bodyLength;
    private String msgType;

    public OnsTraceBean(String localAddress) {
        LOCAL_ADDRESS = localAddress;
        this.storeHost = localAddress;
        this.clientHost = localAddress;
    }
}
