package com.newzkl.platform.base.common.core.job;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

import com.newzkl.platform.base.common.core.job.config.XxlJobConfig;
import com.newzkl.platform.base.common.core.job.config.XxlJobProperties;
import com.newzkl.platform.base.common.core.job.model.XxlJobInfoResp;
import com.newzkl.platform.base.common.core.job.model.XxlJobLogResp;
import com.newzkl.platform.base.common.core.job.model.XxlJobReturnT;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XXL-Job admin REST 客户端
 * <p>由 {@link XxlJobConfig} 装配; 配置项: xxl.job.admin.addresses/username/password,
 * xxl.job.executor.groupIds.
 * @ext cookie session 鉴权
 */
@Slf4j
public class XxlJobAdminClient {

    private volatile String cookie;

    /**
     * 获取配置的所有执行器 jobGroup id 列表
     */
    public List<Integer> getGroupIds() {
        return XxlJobProperties.parseGroupIds();
    }

    /**
     * 任务分页
     * @ext 单 jobGroup
     */
    public XxlJobInfoResp pageJobs(int jobGroup, int triggerStatus, int start, int length) {
        Map<String, Object> form = new HashMap<>();
        form.put("jobGroup", jobGroup);
        form.put("triggerStatus", triggerStatus);
        form.put("start", start);
        form.put("length", length);
        String body = postForm("/jobinfo/pageList", form);
        return JSON.parseObject(body, XxlJobInfoResp.class);
    }

    /**
     * 立即触发
     */
    public XxlJobReturnT<String> trigger(int jobId, String paramJson) {
        Map<String, Object> form = new HashMap<>();
        form.put("id", jobId);
        form.put("executorParam", paramJson == null ? "" : paramJson);
        form.put("addressList", "");
        String body = postForm("/jobinfo/trigger", form);
        return JSON.parseObject(body, new TypeReference<XxlJobReturnT<String>>() {});
    }

    /**
     * 单任务下次 N 次触发时间
     * @ext ms 时间戳
     */
    public XxlJobReturnT<long[]> nextTriggerTimes(String cron, int count) {
        Map<String, Object> form = new HashMap<>();
        form.put("scheduleType", "CRON");
        form.put("scheduleConf", cron);
        form.put("count", count);
        String body = postForm("/jobinfo/nextTriggerTime", form);
        return JSON.parseObject(body, new TypeReference<XxlJobReturnT<long[]>>() {});
    }

    /**
     * 任务执行日志列表
     */
    public XxlJobLogResp pageLogs(int jobId, int start, int length) {
        Map<String, Object> form = new HashMap<>();
        form.put("jobId", jobId);
        form.put("start", start);
        form.put("length", length);
        String body = postForm("/joblog/pageList", form);
        return JSON.parseObject(body, XxlJobLogResp.class);
    }

    private String postForm(String path, Map<String, Object> form) {
        return postForm(path, form, true);
    }

    private String postForm(String path, Map<String, Object> form, boolean retryOnAuthFail) {
        ensureLogin();
        String url = firstAdminUrl() + path;
        try (HttpResponse resp = HttpRequest.post(url)
                .header("Cookie", cookie)
                .form(form)
                .timeout(10_000)
                .execute()) {
            if ((resp.getStatus() == 401 || resp.getStatus() == 302) && retryOnAuthFail) {
                synchronized (this) {
                    cookie = null;
                }
                return postForm(path, form, false);
            }
            return resp.body();
        }
    }

    private synchronized void ensureLogin() {
        if (StrUtil.isNotBlank(cookie)) return;
        String url = firstAdminUrl() + "/login";
        Map<String, Object> form = new HashMap<>();
        form.put("userName", XxlJobProperties.username);
        form.put("password", XxlJobProperties.password);
        try (HttpResponse resp = HttpRequest.post(url).form(form).timeout(10_000).execute()) {
            String setCookie = resp.header("Set-Cookie");
            if (StrUtil.isBlank(setCookie)) {
                throw new IllegalStateException("xxl-job admin login fail: no Set-Cookie header");
            }
            cookie = setCookie.split(";")[0];
            log.info("xxl-job admin login success");
        }
    }

    private String firstAdminUrl() {
        return XxlJobProperties.addresses.split(",")[0].trim();
    }

    /**
     * 按 handler 名查 xxlJobId
     * @ext 启动时调用，本地不做缓存以避免改 cron 后失效
     */
    public Integer findXxlJobIdByHandler(String jobHandler) {
        for (Integer gid : getGroupIds()) {
            XxlJobInfoResp r = pageJobs(gid, -1, 0, 1000);
            if (r == null || r.getData() == null) continue;
            for (XxlJobInfoResp.Item it : r.getData()) {
                if (jobHandler.equals(it.getExecutorHandler())) {
                    return it.getId();
                }
            }
        }
        return null;
    }
}
