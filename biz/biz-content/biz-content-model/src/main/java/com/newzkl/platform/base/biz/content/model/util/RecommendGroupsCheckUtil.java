package com.newzkl.platform.base.biz.content.model.util;

import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

import java.util.Arrays;
import java.util.List;

/**
 * 内容域允许推荐人群类型校验工具类
 *
 * <p>迁移自旧 {@code com.zkl.scm.module.util.RecommendGroupsCheckUtil}。
 * 该类依赖业务枚举({@code RecommendGroupEnum} / {@code RoleEnum}), 不属通用工具,
 * 故落在 {@code biz-content-model} 而非 {@code core-utils}。</p>
 *
 * @author KC
 */
public class RecommendGroupsCheckUtil {

    private RecommendGroupsCheckUtil() {
    }

    /**
     * 校验推荐人群取值合法性
     *
     * <p>入参为逗号分隔的推荐人群名称串, 任一项非法即抛业务异常。</p>
     *
     * @param recommendGroups 逗号分隔的推荐人群名称串
     */
    public static void validateRecommendGroups(String recommendGroups) {
        if (recommendGroups == null || recommendGroups.trim().isEmpty()) {
            ThrowsException.exception(BaseErrorCode.PARAM, "推荐人群不能为空");
        }
        for (String group : recommendGroups.split(",")) {
            String trimmedGroup = group.trim();
            if (!RecommendGroupEnum.contains(trimmedGroup)) {
                ThrowsException.exception(BaseErrorCode.PARAM, "无效的推荐人群: " + trimmedGroup);
            }
        }
    }

    /**
     * 按当前登录角色换算可见的推荐人群范围
     *
     * <p>渠道商仅见 {@code CHANNEL}, C端客户仅见 {@code C_CLIENT}, 其余角色见全部。</p>
     *
     * @param identity 身份id, 取自登录态
     * @return 可见推荐人群名称集合
     */
    public static List<RecommendGroupEnum> getRecommendGroups(AccountEnum.Identity identity) {
        if (AccountEnum.Identity.CHANNEL == identity) {
            return List.of(RecommendGroupEnum.CHANNEL);
        } else if (AccountEnum.Identity.MEMBER == identity) {
            return List.of(RecommendGroupEnum.C_CLIENT);
        }
        return Arrays.asList(RecommendGroupEnum.values());
    }
}
