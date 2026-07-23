package com.newzkl.platform.base.biz.sys.model.dict.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典请求对象。
 *
 * <p>迁移说明: 源 {@code DictReq(DictEnum.Key, Object)} 便捷构造依赖 account 域的
 * 配置枚举 (DictEnum), 属跨域耦合。sys 字典为通用键值存储, 此处保持枚举无关,
 * 由调用方 (各业务域/入口 starter) 自行组装 id/value/desc。</p>
 *
 * @author fang
 */
@Data
@NoArgsConstructor
public class DictReq {
    /**
     * 字典id
     */
    private Long id;
    /**
     * 字典值
     */
    private String value;
    /**
     * 字典描述
     */
    private String desc;
}
