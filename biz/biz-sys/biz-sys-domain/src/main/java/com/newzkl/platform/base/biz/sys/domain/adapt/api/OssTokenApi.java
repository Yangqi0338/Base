package com.newzkl.platform.base.biz.sys.domain.adapt.api;

/**
 * 对象存储上传凭证出站端口
 *
 * <p>实现方需自行封装厂商签名细节, 领域侧只认"拿到一个可直传的上传凭证"</p>
 *
 * @author KC
 */
public interface OssTokenApi {

    /**
     * 获取默认存储空间的上传凭证
     *
     * @return 上传凭证串, 供前端直传对象存储使用
     */
    String uploadToken();
}
