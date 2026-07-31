package com.newzkl.platform.base.biz.sys.facade;

/**
 * @author muc_fang
 * @Description: 字典
 * @date 2023/12/1411:51
 */
public interface IDictFacade {
    String get(Long id);

    void set(Long id, String value);
}
