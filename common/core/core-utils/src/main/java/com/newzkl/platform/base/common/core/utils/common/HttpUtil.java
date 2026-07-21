package com.newzkl.platform.base.common.core.utils.common;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * HTTP 连接工具。
 *
 * @author niu
 */
@Slf4j
public class HttpUtil {

    public static URLConnection openConnection(URL url) throws IOException {
        return url.openConnection();
    }
}
