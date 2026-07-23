package com.newzkl.platform.base.biz.account.model.vo;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import lombok.Data;

import java.util.*;

/**
 * @author fang
 */
@Data
public class AccountStructureTreeVO extends AccountStructureVO {
    /**
     * 角色id
     *
     */
    private String roleIdList;
    /**
     * 父id
     *
     */
    private Long pid;
    /**
     * 父id
     *
     */
    private String pidList;
    /**
     * 层级
     *
     */
    private Integer level;
    /**
     * 下级账号Tree列表
     *
     */
    private List<AccountStructureTreeVO> children;
    /**
     * 下级账号数量
     *
     */
    private Integer subAccountCount;

    @JsonIgnore
    private Map<RoleEnum.CompanyRole, Integer> subAccountCountMap;

    public static List<AccountStructureTreeVO> buildTotalCount(CommonEnum.Client client, List<AccountStructureTreeVO> accountSubStructureVOList) {
        accountSubStructureVOList.forEach(it -> it.buildTotalCount(client));
        return accountSubStructureVOList;
    }

    /**
     * 构建树形结构
     *
     * @param pid                       初始父ID（作为根节点的父ID）
     * @param accountSubStructureVOList 所有节点列表
     * @return 构建后的树形列表（根节点集合）
     */
    public static List<AccountStructureTreeVO> buildTree(Long pid, List<AccountStructureTreeVO> accountSubStructureVOList) {
        if (CollUtil.isEmpty(accountSubStructureVOList)) {
            return Collections.emptyList();
        }

        // 1. 构建ID到节点的映射（方便快速查找父节点）
        Map<Long, AccountStructureTreeVO> nodeMap = accountSubStructureVOList.stream()
                .collect(CommonUtil.toMap(BaseRes::getId));

        // 2. 初始化所有节点的children和子节点数量
        accountSubStructureVOList.forEach(node -> {
            node.children = new ArrayList<>();
        });

        // 3. 构建父子关系，同时收集根节点（无父节点或父节点不在映射中的节点）
        List<AccountStructureTreeVO> roots = new ArrayList<>();
        for (AccountStructureTreeVO node : accountSubStructureVOList) {
            Long parentId = node.getPid();
            if (!nodeMap.containsKey(parentId)) {
                roots.add(node);
            } else {
                AccountStructureTreeVO parent = nodeMap.get(parentId);
                parent.getChildren().add(node);
            }
        }

        // 4. 递归设置层级（从 "1" 开始）
        for (AccountStructureTreeVO root : roots) {
            setLevel(root, 1);
        }

        // 5. 根据传入的 pid 返回对应节点下的直接子节点，否则返回所有根节点
        if (nodeMap.containsKey(pid)) {
            AccountStructureTreeVO target = nodeMap.get(pid);
            return target.children;
        }
        return roots;
    }

    private static void setLevel(AccountStructureTreeVO node, Integer level) {
        node.setLevel(level);
        if (CollUtil.isNotEmpty(node.children)) {
            for (AccountStructureTreeVO child : node.children) {
                setLevel(child, level + 1);
            }
        }
    }

    public static List<AccountStructureTreeVO> buildTree(List<AccountStructureTreeVO> accountSubStructureVOList) {
        return buildTree(0L, accountSubStructureVOList);
    }

    public void buildTotalCount(CommonEnum.Client client) {
        this.subAccountCountMap = new HashMap<>();
        this.subAccountCount = 0;
        if (CollUtil.isNotEmpty(children)) {
            children.forEach(subAccount -> {
                if (MapUtil.isEmpty(subAccount.getSubAccountCountMap())) {
                    subAccount.buildTotalCount(client);
                }
                if (MapUtil.isNotEmpty(subAccount.getSubAccountCountMap())) {
                    RoleEnum.CompanyRole role = Opt.ofNullable(CollUtil.getLast(RoleEnumUtil.getLevelUpEnumList(client, subAccount.getRoleIdList())))
                            .orElse(
                                    RoleEnum.CompanyRole.getByCode(
                                            NumberUtil.parseLong(
                                                    CollUtil.getLast(
                                                            StrUtil.split(subAccount.getRoleIdList(), ",")
                                                    ))));

                    subAccountCountMap.compute(role, (k, v) -> v == null ? 1 : v + 1);
                    subAccountCount += 1;
                }
            });
        }
    }

    public List<AccountStructureTreeVO> getChildren(CommonEnum.Client client) {
        List<AccountStructureTreeVO> accountSubStructureVOList = CollUtil.isNotEmpty(RoleEnumUtil.getLevelUpEnumList(client, roleIdList)) ?
                CollUtil.newArrayList(this) : new ArrayList<>();
        if (CollUtil.isNotEmpty(children)) {
            children.forEach(subAccount -> {
                List<AccountStructureTreeVO> children = subAccount.getChildren(client);
                accountSubStructureVOList.addAll(children);
            });
        }
        return accountSubStructureVOList;
    }
}