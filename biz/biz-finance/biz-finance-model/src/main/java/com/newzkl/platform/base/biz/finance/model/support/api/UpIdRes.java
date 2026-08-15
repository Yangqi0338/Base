package com.newzkl.platform.base.biz.finance.model.support.api;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 上级链路结果 (跨域 user 结构降级为 finance 本地 DTO)
 *
 * <p>迁移: 原 {@code com.zkl.scm.user.model.account.res.UpIdRes};
 * 原依赖 {@code BizUtil.getOperatorLevelUpEnumList} 已内联, 去除跨域耦合。</p>
 *
 * @author muc_fang
 */
@Data
public class UpIdRes implements Serializable {
    /**
     * roleId列表
     */
    private String roleIdList;
    /**
     * 直属上级ID
     */
    private Long oneId;
    /**
     * 直属上级RoleId
     */
    private RoleEnum.CompanyRole directRoleId;
    /**
     * 多级上级ID
     */
    private List<Long> upId;
    /**
     * 多级RoleId
     */
    private List<String> pRoleIdList;
}
