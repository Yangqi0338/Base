package com.newzkl.platform.base.biz.sys.facade;

/**
 * @author muc_fang
 * @Description: 字典
 * @date 2023/12/1411:51
 */
public interface IDictFacade {
    /**
     * 按业务键取字典值
     *
     * @param code 字典业务键(DictEnum.Key 写死码值)
     * @return 字典值, 不存在返回 null
     */
    String get(Long code);

    /**
     * 按业务键写入字典值
     *
     * @param code  字典业务键(DictEnum.Key 写死码值)
     * @param value 字典值
     */
    void set(Long code, String value);
}
