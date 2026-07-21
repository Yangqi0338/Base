package com.newzkl.platform.base.biz.account.infrastructure.auth.repository;
import com.newzkl.platform.base.biz.account.infrastructure.support.PageBuildUtil;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.account.model.enums.AuthEnum;
import com.newzkl.platform.base.biz.account.model.enums.RedisEnum;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.account.domain.auth.repository.AuthRepository;
import com.newzkl.platform.base.biz.account.infrastructure.auth.dao.AuthFunctionDAO;
import com.newzkl.platform.base.biz.account.infrastructure.auth.dao.AuthMenuDAO;
import com.newzkl.platform.base.biz.account.infrastructure.auth.dao.AuthRelationsDAO;
import com.newzkl.platform.base.biz.account.infrastructure.auth.dao.AuthRoleDAO;
import com.newzkl.platform.base.biz.account.infrastructure.auth.entity.AuthFunctionDO;
import com.newzkl.platform.base.biz.account.infrastructure.auth.entity.AuthMenuDO;
import com.newzkl.platform.base.biz.account.infrastructure.auth.entity.AuthRelationsDO;
import com.newzkl.platform.base.biz.account.infrastructure.auth.entity.AuthRoleDO;
import com.newzkl.platform.base.biz.account.model.auth.req.*;
import com.newzkl.platform.base.biz.account.model.auth.vo.FunctionVO;
import com.newzkl.platform.base.biz.account.model.auth.vo.InterfaceVO;
import com.newzkl.platform.base.biz.account.model.auth.vo.LimitRoleVO;
import com.newzkl.platform.base.biz.account.model.auth.vo.MenuTreeVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthRoleDAO authRoleDAO;
    private final AuthFunctionDAO authFunctionDAO;
    private final AuthMenuDAO authMenuDAO;
    private final AuthRelationsDAO authRelationsDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleCreate(RoleReq roleReq) {
        AuthRoleDO roleDO = TransferUtils.transfer(roleReq, AuthRoleDO::new);
        authRoleDAO.insert(roleDO);

        //新增角色与系统的关联
        AuthRelationsDO authRelationsDO = new AuthRelationsDO();
        authRelationsDO.setCreateId(roleReq.getCreateId());
        authRelationsDO.setType(AuthEnum.RelationType.SYS_ROLE.name());
        authRelationsDO.setSourceId(roleReq.getSystemId());
        authRelationsDO.setTargetId(roleDO.getId());
        authRelationsDAO.insert(authRelationsDO);
    }

    @Override
    public void roleEdit(RoleReq roleReq) {
        authRoleDAO.updateById(TransferUtils.transfer(roleReq, AuthRoleDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleDelete(Long roleId) {
        boolean exists = authRelationsDAO.exists(new LambdaUpdateWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getTargetId, roleId).eq(AuthRelationsDO::getType, AuthEnum.RelationType.USER_ROLE.name()));
        if (exists) {
            throw new ScmException(-1, "已绑定用户的角色无法删除");
        }
        //删除角色
        authRoleDAO.deleteById(roleId);
        //删除角色与功能点、菜单、系统的关联
        authRelationsDAO.delete(new LambdaUpdateWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getSourceId, roleId).or().eq(AuthRelationsDO::getTargetId, roleId));
    }

    @Override
    public Page<LimitRoleVO> rolePage(RolePageQuery query) {
        List<Long> menuIdList = authRelationsDAO.selectList(new BaseLambdaQueryWrapper<AuthRelationsDO>()
                .eq(AuthRelationsDO::getType, AuthEnum.RelationType.SYS_MENU.name())
                .eq(AuthRelationsDO::getSourceId, query.getSystemId())
        ).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toList());
        Page<AuthRoleDO> limitRoleDOPage = authRoleDAO.selectPage(PageBuildUtil.of(query), new BaseLambdaQueryWrapper<AuthRoleDO>()
                .notEmptyLike(AuthRoleDO::getName, query.getName())
                .notEmptyIn(AuthRoleDO::getId, query.getIdList())
                .notNullEq(AuthRoleDO::getId, query.getId())
                .notEmptyIn(AuthRoleDO::getId, menuIdList)
        );
        return TransferUtils.transferPage(limitRoleDOPage, LimitRoleVO::new);
    }

    @Override
    public void createFunction(FunctionReq req) {
        authFunctionDAO.insert(TransferUtils.transfer(req, AuthFunctionDO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFunction(FunctionReq req) {
        // 1. 查询原有功能点信息
        AuthFunctionDO oldFunction = authFunctionDAO.selectOne(new BaseLambdaQueryWrapper<AuthFunctionDO>().eq(AuthFunctionDO::getId, req.getId())
        );

        // 2. 检查类型是否从 FUNCTION 变为 DEFAULT
        if (oldFunction != null && AuthEnum.FunctionType.FUNCTION.name().equals(oldFunction.getType()) && AuthEnum.FunctionType.DEFAULT.name().equals(req.getType())) {

            // 3. 删除所有角色与该功能点的关联
            authRelationsDAO.delete(new LambdaUpdateWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getTargetId, req.getId()).eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_API.name())
            );

            log.info("功能点类型从 FUNCTION 变为 DEFAULT，已删除所有角色关联: id={}", req.getId());
        }

        // 4. 更新功能点信息
        authFunctionDAO.update(TransferUtils.transfer(req, AuthFunctionDO::new), new BaseLambdaQueryWrapper<AuthFunctionDO>().eq(AuthFunctionDO::getId, req.getId())
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunction(Long id) {
        // 检查是否有已绑定角色的功能点
        boolean exists = authRelationsDAO.exists(new LambdaUpdateWrapper<AuthRelationsDO>()
                .eq(AuthRelationsDO::getTargetId, id)
                .eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_API.name())
        );

        if (exists) {
            // 检查是否为废弃接口
            List<FunctionVO> deprecatedFunctions = this.deprecatedFunctionList();
            if (CollectionUtil.isNotEmpty(deprecatedFunctions)) {
                List<Long> deprecatedCodes = deprecatedFunctions.stream().map(FunctionVO::getId)
                        .collect(Collectors.toList());

                // 如果不是废弃接口，则不能删除
                if (!deprecatedCodes.contains(id)) {
                    throw new ScmException(-1, "已绑定角色的权限无法删除: " + id);
                }
            }
        }

        // 3. 删除功能点
        authFunctionDAO.delete(new BaseLambdaQueryWrapper<AuthFunctionDO>().in(AuthFunctionDO::getId, id));
    }

    @Override
    public Page<FunctionVO> functionPage(FunctionPageQuery query) {
        Page<AuthFunctionDO> authFunctionDOPage = authFunctionDAO.selectPage(PageBuildUtil.of(query),
                authFunctionDAO.buildQueryWrapper(TransferUtils.transfer(query, AuthFunctionDO::new))
                        .notEmptyIn(AuthFunctionDO::getId, query.getIdList())
                        .orderByDesc(AuthFunctionDO::getId)
        );
        return TransferUtils.transferPage(authFunctionDOPage, FunctionVO::new);
    }

    @Override
    public List<MenuTreeVO> functionTreeList(FunctionTreeQuery functionTreeQuery) {
        // 1. 查询所有菜单
        List<Long> menuIdList = new ArrayList<>();
        if (functionTreeQuery.getSystemId() != null) {
            menuIdList = authRelationsDAO.selectList(new BaseLambdaQueryWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getType, AuthEnum.RelationType.SYS_MENU.name()).eq(AuthRelationsDO::getSourceId, functionTreeQuery.getSystemId())).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toList());
        } else {
            menuIdList = authRelationsDAO.selectList(new BaseLambdaQueryWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_MENU.name()).eq(AuthRelationsDO::getSourceId, functionTreeQuery.getRoleId())).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toList());
        }
        if (CollectionUtil.isEmpty(menuIdList)) {
            return Collections.emptyList();
        }
        List<AuthMenuDO> allMenus = authMenuDAO.selectList(new BaseLambdaQueryWrapper<AuthMenuDO>().in(AuthMenuDO::getId, menuIdList));


        // 2. 查询所有功能点
        List<AuthFunctionDO> allFunctions = authFunctionDAO.selectList(null);

        // 3. 查询该角色已分配的菜单ID和功能点ID集合
        Set<Long> assignedMenuIds = authRelationsDAO.selectList(new BaseLambdaQueryWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_MENU.name()).eq(AuthRelationsDO::getSourceId, functionTreeQuery.getRoleId())).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toSet());
        Set<Long> assignedFunctionIds = authRelationsDAO.selectList(new BaseLambdaQueryWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_API.name()).eq(AuthRelationsDO::getSourceId, functionTreeQuery.getRoleId())).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toSet());


        // 4. 查询菜单-功能点关联关系
        List<AuthRelationsDO> menuFunctionRelations = authRelationsDAO.selectList(
                new BaseLambdaQueryWrapper<AuthRelationsDO>().notEmptyIn(AuthRelationsDO::getSourceId, menuIdList)
                        .eq(AuthRelationsDO::getType, AuthEnum.RelationType.MENU_API.name())
        );
        // 构建菜单ID到功能点ID列表的映射
        Map<Long, List<Long>> menuToFunctionsMap = menuFunctionRelations.stream()
                .collect(Collectors.groupingBy(
                        AuthRelationsDO::getSourceId,
                        Collectors.mapping(AuthRelationsDO::getTargetId, Collectors.toList())
                ));

        // 5. 构建功能点ID到对象的映射
        Map<Long, AuthFunctionDO> functionMap = CollectionUtil.isEmpty(allFunctions)
                ? Collections.emptyMap()
                : allFunctions.stream().collect(Collectors.toMap(AuthFunctionDO::getId, f -> f, (v1, v2) -> v1));

        // 6. 转换为MenuTreeVO
        List<MenuTreeVO> allMenuNodes = allMenus.stream()
                .map(menu -> {
                    MenuTreeVO menuVO = new MenuTreeVO();
                    menuVO.setId(menu.getId());
                    menuVO.setName(menu.getName());
                    menuVO.setSort(menu.getSort());
                    menuVO.setAssigned(assignedMenuIds.contains(menu.getId()));
                    menuVO.setChildMenu(new ArrayList<>());

                    // 获取该菜单下的功能点
                    List<Long> functionIds = menuToFunctionsMap.get(menu.getId());
                    if (CollectionUtil.isNotEmpty(functionIds)) {
                        List<MenuTreeVO.FunctionVO> childFunctions = functionIds.stream()
                                .map(functionId -> {
                                    AuthFunctionDO functionDO = functionMap.get(functionId);
                                    if (functionDO != null) {
                                        MenuTreeVO.FunctionVO functionVO = new MenuTreeVO.FunctionVO();
                                        functionVO.setId(functionDO.getId());
                                        functionVO.setName(functionDO.getName());
                                        functionVO.setAssigned(assignedFunctionIds.contains(functionDO.getId()));
                                        return functionVO;
                                    }
                                    return null;
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        menuVO.setChildFunction(childFunctions);
                    } else {
                        menuVO.setChildFunction(new ArrayList<>());
                    }

                    return menuVO;
                })
                .collect(Collectors.toList());

        // 7. 构建树形结构
        return buildMenuTree(allMenus, allMenuNodes);
    }

    /**
     * 构建菜单树形结构
     *
     * @param allMenus     所有菜单DO列表
     * @param allMenuNodes 所有菜单VO列表
     * @return 树形结构根节点列表
     */
    private List<MenuTreeVO> buildMenuTree(List<AuthMenuDO> allMenus, List<MenuTreeVO> allMenuNodes) {
        if (CollectionUtil.isEmpty(allMenus) || CollectionUtil.isEmpty(allMenuNodes)) {
            return Collections.emptyList();
        }

        // 构建ID到VO节点的映射
        Map<Long, MenuTreeVO> idToNodeMap = new HashMap<>();
        for (int i = 0; i < allMenus.size() && i < allMenuNodes.size(); i++) {
            idToNodeMap.put(allMenus.get(i).getId(), allMenuNodes.get(i));
        }

        // 根节点列表（affiliatedMenuId为空或父节点不存在的节点）
        List<MenuTreeVO> rootNodes = new ArrayList<>();

        for (AuthMenuDO menuDO : allMenus) {
            Long parentId = menuDO.getAffiliatedMenuId();
            MenuTreeVO currentNode = idToNodeMap.get(menuDO.getId());

            if (currentNode == null) {
                continue;
            }

            // 如果没有父节点ID，则作为根节点
            if (parentId == null) {
                rootNodes.add(currentNode);
            } else {
                MenuTreeVO parentNode = idToNodeMap.get(parentId);
                if (parentNode != null) {
                    // 找到父节点并添加到父节点的children中
                    parentNode.getChildMenu().add(currentNode);
                } else {
                    // 父节点不存在，作为根节点
                    rootNodes.add(currentNode);
                }
            }
        }

        // 对根节点按sort排序
        rootNodes.sort(Comparator.comparing(MenuTreeVO::getSort, Comparator.nullsLast(Comparator.naturalOrder())));

        // 递归对所有子节点排序
        sortChildMenus(rootNodes);

        return rootNodes;
    }

    /**
     * 递归对子菜单排序
     *
     * @param menus 菜单列表
     */
    private void sortChildMenus(List<MenuTreeVO> menus) {
        if (CollectionUtil.isEmpty(menus)) {
            return;
        }
        for (MenuTreeVO menu : menus) {
            if (CollectionUtil.isNotEmpty(menu.getChildMenu())) {
                // 对子菜单排序
                menu.getChildMenu().sort(Comparator.comparing(MenuTreeVO::getSort, Comparator.nullsLast(Comparator.naturalOrder())));
                // 递归处理子菜单
                sortChildMenus(menu.getChildMenu());
            }
        }
    }

    @Override
    public List<FunctionVO> noEnteredFunctionList() {
        // 1. 获取项目中所有的接口urlPath
        // 1.1 获取所有匹配INTERFACE_KEY_PREFIX的keys
        Iterable<String> keys = RedisUtil.getKeysByPattern(RedisEnum.Key.INTERFACE_KEY_PREFIX + "*");

        // 1.2 遍历所有keys，获取对应的InterfaceTreeVO列表
        List<InterfaceVO> resourceTree = new ArrayList<>();
        for (String key : keys) {
            String jsonData = RedisUtil.get(key);
            if (StrUtil.isNotBlank(jsonData)) {
                List<InterfaceVO> interfaceList = JSON.parseArray(jsonData, InterfaceVO.class);
                if (CollectionUtil.isNotEmpty(interfaceList)) {
                    resourceTree.addAll(interfaceList);
                }
            }
        }

        // 1.3 将所有urlPath放入projectUrlPaths集合
        Set<String> projectUrlPaths = resourceTree.stream()
                .map(InterfaceVO::getUrlPath)
                .collect(Collectors.toSet());

        if (CollectionUtil.isEmpty(projectUrlPaths)) {
            return Collections.emptyList();
        }

        // 2. 查询数据库中所有的urlPath
        List<AuthFunctionDO> allFunctions = authFunctionDAO.selectList(new BaseLambdaQueryWrapper<AuthFunctionDO>().eq(AuthFunctionDO::getType, AuthEnum.FunctionType.FUNCTION.name())
        );
        Set<String> dbUrlPaths = allFunctions.stream().map(AuthFunctionDO::getUrlPath)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());

        // 3. 找出项目中存在但数据库中不存在的urlPath
        Set<String> notEnteredPaths = new HashSet<>(projectUrlPaths);
        notEnteredPaths.removeAll(dbUrlPaths);

        if (CollectionUtil.isEmpty(notEnteredPaths)) {
            return Collections.emptyList();
        }

        // 4. 构建FunctionVO列表（从项目接口信息中获取）
        List<FunctionVO> result = new ArrayList<>();
        for (InterfaceVO interfaceVO : resourceTree) {
            if (notEnteredPaths.contains(interfaceVO.getUrlPath())) {
                FunctionVO vo = TransferUtils.transfer(interfaceVO, FunctionVO::new);
                result.add(vo);
            }
        }

        return result;
    }

    @Override
    public List<FunctionVO> deprecatedFunctionList() {
        // 1. 获取项目中所有的接口urlPath
        // 1.1 获取所有匹配INTERFACE_KEY_PREFIX的keys
        Iterable<String> keys = RedisUtil.getKeysByPattern(RedisEnum.Key.INTERFACE_KEY_PREFIX + "*");

        // 1.2 遍历所有keys，获取对应的InterfaceTreeVO列表
        List<InterfaceVO> resourceTree = new ArrayList<>();
        for (String key : keys) {
            String jsonData = RedisUtil.get(key);
            if (StrUtil.isNotBlank(jsonData)) {
                List<InterfaceVO> interfaceList = JSON.parseArray(jsonData, InterfaceVO.class);
                if (CollectionUtil.isNotEmpty(interfaceList)) {
                    resourceTree.addAll(interfaceList);
                }
            }
        }

        // 1.3 将所有urlPath放入projectUrlPaths集合
        Set<String> projectUrlPaths = resourceTree.stream().map(InterfaceVO::getUrlPath).collect(Collectors.toSet());

        // 2. 查询数据库中所有的功能点
        List<AuthFunctionDO> allFunctions = authFunctionDAO.selectList(new BaseLambdaQueryWrapper<AuthFunctionDO>().eq(AuthFunctionDO::getType, AuthEnum.FunctionType.FUNCTION.name())
        );
        if (CollectionUtil.isEmpty(allFunctions)) {
            return Collections.emptyList();
        }

        // 3. 找出数据库中存在但项目中不存在的urlPath
        return allFunctions.stream()
                .filter(func -> StrUtil.isNotBlank(func.getUrlPath())) // urlPath不为空
                .filter(func -> !projectUrlPaths.contains(func.getUrlPath())) // 项目中不存在
                .map(func -> TransferUtils.transfer(func, FunctionVO::new))
                .collect(Collectors.toList());
    }

    @Override
    public void cacheUserUnFunctionUrls(Long accountId) {
        try {
            // 1. 查询用户的所有角色ID
            List<AuthRelationsDO> authRelationsDOS = authRelationsDAO.selectList(new BaseLambdaQueryWrapper<AuthRelationsDO>()
                    .eq(AuthRelationsDO::getSourceId, accountId)
                    .eq(AuthRelationsDO::getType, AuthEnum.RelationType.USER_ROLE.name())
            );

            // 2. 收集所有角色ID
            List<Long> roleIds = authRelationsDOS.stream().map(AuthRelationsDO::getTargetId).distinct().collect(Collectors.toList());

            // 3. 查询这些角色已分配的功能点Id集合
            Set<Long> assignedFunctionIds = new HashSet<>();
            for (Long roleId : roleIds) {
                List<AuthRelationsDO> roleFunctions = authRelationsDAO.selectList(new BaseLambdaQueryWrapper<AuthRelationsDO>()
                        .eq(AuthRelationsDO::getSourceId, roleId)
                        .eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_API.name())
                );
                roleFunctions.forEach(rf -> assignedFunctionIds.add(rf.getTargetId()));
            }

            // 4. 查询用户没有的权限
            List<AuthFunctionDO> functions = authFunctionDAO.selectList(new BaseLambdaQueryWrapper<AuthFunctionDO>()
                    .notEmptyIn(AuthFunctionDO::getId, assignedFunctionIds)
                    .in(AuthFunctionDO::getType, AuthEnum.FunctionType.FUNCTION.name())
            );

            log.info("用户: {} 没有的权限：{}", accountId, functions);

            // 5. 提取urlPath并存入Redis
            String redisKey = RedisEnum.Key.UN_PERMISSION_KEY_PREFIX.getCode() + accountId;
            // 先删除旧缓存
            RedisUtil.del(redisKey);
            // 提取所有urlPath并存入Redis
            List<String> urlPaths = functions.stream().map(AuthFunctionDO::getUrlPath).filter(StrUtil::isNotBlank).collect(Collectors.toList());

            RedisUtil.sAdd(redisKey, urlPaths.toArray());
            log.info("用户 {} 权限缓存成功，共 {} 个URL", accountId, urlPaths.size());
        } catch (Exception e) {
            log.info("保存用户没有的鉴权接口数据失败，用户Id:{},Message:{}", accountId, e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMenu(MenuReq req) {
        AuthMenuDO menuDO = TransferUtils.transfer(req, AuthMenuDO::new);
        authMenuDAO.insert(menuDO);

        AuthRelationsDO relation = new AuthRelationsDO();
        relation.setCreateId(req.getCreateId());
        relation.setType(AuthEnum.RelationType.SYS_MENU.name());
        relation.setSourceId(req.getSystemId());
        relation.setTargetId(menuDO.getId());
        authRelationsDAO.insert(relation);
    }

    @Override
    public void updateMenu(MenuReq req) {
        AuthMenuDO menuDO = TransferUtils.transfer(req, AuthMenuDO::new);
        authMenuDAO.updateById(menuDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        boolean exists = authRelationsDAO.exists(new LambdaUpdateWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getTargetId, id).eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_MENU.name()));
        if (exists) {
            throw new ScmException(-1, "已绑定角色的菜单无法删除");
        }
        authMenuDAO.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkFunction(FunctionRelationsReq req) {
        List<AuthRelationsDO> list = new ArrayList<>();

        // 根据关系类型分别处理
        if (AuthEnum.RelationType.ROLE_MENU.name().equals(req.getType())) {
            // 勾选菜单：建立菜单及其子菜单、功能点的关系
            handleMenuRelations(req, list);
        } else {
            // 勾选功能点：建立功能点关系及其所属菜单的关系
            handleFunctionRelations(req, list);
        }

        if (CollectionUtil.isNotEmpty(list)) {
            authRelationsDAO.insert(list);
        }
    }

    /**
     * 处理菜单关系：建立角色与菜单、子菜单及其功能点的关系
     */
    private void handleMenuRelations(FunctionRelationsReq req, List<AuthRelationsDO> list) {
        AuthMenuDO menuDO = authMenuDAO.selectById(req.getMenuId());
        if (menuDO == null) {
            return;
        }

        // 1. 收集需要关联的所有菜单ID
        List<Long> allMenuIds = new ArrayList<>();
        allMenuIds.add(req.getMenuId());

        // 2. 如果是父菜单，添加所有子菜单关系
        if (menuDO.getAffiliatedMenuId() == null) {
            List<Long> childMenuIds = getAllChildMenuIds(req.getMenuId());
            for (Long childMenuId : childMenuIds) {
                list.add(buildRelation(req.getRoleId(), childMenuId, req.getType()));
            }
            allMenuIds.addAll(childMenuIds);
        } else {
            // 如果是子菜单，也需要包含父菜单
            allMenuIds.add(menuDO.getAffiliatedMenuId());
        }

        // 3. 添加角色与菜单以及菜单关联的所有功能点关系
        addMenuFunctionRelations(req.getRoleId(), allMenuIds, list);
    }

    /**
     * 处理功能点关系：建立角色与功能点及其所属菜单（包括父菜单）的关系
     */
    private void handleFunctionRelations(FunctionRelationsReq req, List<AuthRelationsDO> list) {
        // 1. 建立功能点关系
        list.add(buildRelation(req.getRoleId(), req.getFunctionId(), req.getType()));

        // 2. 查询该功能点关联的菜单
        AuthMenuDO authMenuDO = authMenuDAO.selectOne(
                new BaseLambdaQueryWrapper<AuthMenuDO>()
                        .eq(AuthMenuDO::getId, req.getMenuId())
        );

        // 3. 添加当前菜单的关系
        list.add(buildRelation(req.getRoleId(), authMenuDO.getId(), AuthEnum.RelationType.ROLE_MENU.name()));

        if (authMenuDO.getAffiliatedMenuId() != null) {
            // 4. 添加当前菜单的父菜单的关系
            list.add(buildRelation(req.getRoleId(), authMenuDO.getAffiliatedMenuId(), AuthEnum.RelationType.ROLE_MENU.name()));
        }
    }

    /**
     * 添加菜单关联的功能点关系
     */
    private void addMenuFunctionRelations(Long roleId, List<Long> menuIds, List<AuthRelationsDO> list) {
        if (CollectionUtil.isEmpty(menuIds)) {
            return;
        }

        // 获取该角色已关联的菜单id
        List<Long> linkedMenuIdList = authRelationsDAO.selectList(
                new BaseLambdaQueryWrapper<AuthRelationsDO>()
                        .eq(AuthRelationsDO::getSourceId, roleId)
                        .eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_MENU.name())
        ).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toList());
        List<Long> toBeLinkMenuIds = new ArrayList<>(menuIds);
        toBeLinkMenuIds.removeAll(linkedMenuIdList);
        for (Long menuId : toBeLinkMenuIds) {
            list.add(buildRelation(roleId, menuId, AuthEnum.RelationType.ROLE_MENU.name()));
        }

        // 获取该角色已关联的功能点id
        List<Long> linkedFunctionIdList = authRelationsDAO.selectList(
                new BaseLambdaQueryWrapper<AuthRelationsDO>()
                        .eq(AuthRelationsDO::getSourceId, roleId)
                        .eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_API.name())
        ).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toList());

        List<Long> menuFunctionIdList = authRelationsDAO.selectList(
                new BaseLambdaQueryWrapper<AuthRelationsDO>()
                        .in(AuthRelationsDO::getSourceId, menuIds)
                        .eq(AuthRelationsDO::getType, AuthEnum.RelationType.MENU_API.name())
        ).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toList());

        menuFunctionIdList.removeAll(linkedFunctionIdList);
        for (Long menuFunctionId : menuFunctionIdList) {
            list.add(buildRelation(roleId, menuFunctionId, AuthEnum.RelationType.ROLE_API.name()));
        }
    }

    /**
     * 构建关系对象
     */
    private AuthRelationsDO buildRelation(Long sourceId, Long targetId, String type) {
        AuthRelationsDO relation = new AuthRelationsDO();
        relation.setSourceId(sourceId);
        relation.setTargetId(targetId);
        relation.setType(type);
        relation.setCreateId(SecurityUtils.getAccountId());
        return relation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelCheckFunction(FunctionRelationsReq req) {
        // 根据关系类型分别处理
        if (AuthEnum.RelationType.ROLE_MENU.name().equals(req.getType())) {
            // 取消勾选菜单：删除菜单及其子菜单、功能点的关系
            handleCancelMenuRelations(req);
        } else {
            // 取消勾选功能点：仅删除功能点关系
            authRelationsDAO.delete(
                    new LambdaUpdateWrapper<AuthRelationsDO>()
                            .eq(AuthRelationsDO::getType, req.getType())
                            .eq(AuthRelationsDO::getSourceId, req.getRoleId())
                            .eq(AuthRelationsDO::getTargetId, req.getFunctionId())
            );
        }
    }

    /**
     * 处理取消菜单关系：删除角色与菜单、子菜单及其功能点的关系
     */
    private void handleCancelMenuRelations(FunctionRelationsReq req) {
        // 1. 收集所有需要删除的菜单ID（包括当前菜单和所有子菜单）
        List<Long> allMenuIds = new ArrayList<>();
        allMenuIds.add(req.getMenuId());
        allMenuIds.addAll(getAllChildMenuIds(req.getMenuId()));

        // 2. 删除角色与这些菜单的关系
        authRelationsDAO.delete(
                new LambdaUpdateWrapper<AuthRelationsDO>()
                        .eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_MENU.name())
                        .eq(AuthRelationsDO::getSourceId, req.getRoleId())
                        .in(AuthRelationsDO::getTargetId, allMenuIds)
        );

        // 3. 查询这些菜单关联的所有功能点
        List<AuthRelationsDO> menuFunctionRelations = authRelationsDAO.selectList(
                new BaseLambdaQueryWrapper<AuthRelationsDO>()
                        .in(AuthRelationsDO::getSourceId, allMenuIds)
                        .eq(AuthRelationsDO::getType, AuthEnum.RelationType.MENU_API.name())
        );

        if (CollectionUtil.isNotEmpty(menuFunctionRelations)) {
            List<Long> functionIds = menuFunctionRelations.stream()
                    .map(AuthRelationsDO::getTargetId)
                    .distinct()
                    .collect(Collectors.toList());

            // 4. 删除角色与这些功能点的关系
            authRelationsDAO.delete(
                    new LambdaUpdateWrapper<AuthRelationsDO>()
                            .eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_API.name())
                            .eq(AuthRelationsDO::getSourceId, req.getRoleId())
                            .in(AuthRelationsDO::getTargetId, functionIds)
            );
        }
    }

    @Override
    public List<MenuTreeVO> roleFunctionTreeList(Long roleId) {
        // 1. 查询角色的所有菜单
        List<Long> menuIdList = authRelationsDAO.selectList(new BaseLambdaQueryWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_MENU.name()).eq(AuthRelationsDO::getSourceId, roleId)).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(menuIdList)) {
            return Collections.emptyList();
        }
        List<AuthMenuDO> allMenus = authMenuDAO.selectList(new BaseLambdaQueryWrapper<AuthMenuDO>().in(AuthMenuDO::getId, menuIdList));

        // 2. 查询角色的所有功能点
        List<Long> assignedFunctionIds = authRelationsDAO.selectList(new BaseLambdaQueryWrapper<AuthRelationsDO>().eq(AuthRelationsDO::getType, AuthEnum.RelationType.ROLE_API.name()).eq(AuthRelationsDO::getSourceId, roleId)).stream().map(AuthRelationsDO::getTargetId).collect(Collectors.toList());

        // 3. 查询菜单-功能点关联关系
        List<AuthRelationsDO> menuFunctionRelations = new ArrayList<>();
        // 4. 构建功能点ID到对象的映射
        Map<Long, AuthFunctionDO> functionMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(assignedFunctionIds)) {
            List<AuthFunctionDO> allFunctions = authFunctionDAO.selectList(
                    new BaseLambdaQueryWrapper<AuthFunctionDO>()
                            .notEmptyIn(AuthFunctionDO::getId, assignedFunctionIds)
            );
            menuFunctionRelations = authRelationsDAO.selectList(
                    new BaseLambdaQueryWrapper<AuthRelationsDO>()
                            .notEmptyIn(AuthRelationsDO::getSourceId, menuIdList)
                            .notEmptyIn(AuthRelationsDO::getTargetId, assignedFunctionIds)
                            .eq(AuthRelationsDO::getType, AuthEnum.RelationType.MENU_API.name())
            );
            functionMap = CollectionUtil.isEmpty(allFunctions) ? Collections.emptyMap() : allFunctions.stream().collect(Collectors.toMap(AuthFunctionDO::getId, f -> f, (v1, v2) -> v1));
        }
        // 构建菜单ID到功能点ID列表的映射
        Map<Long, List<Long>> menuToFunctionsMap = menuFunctionRelations.stream().collect(Collectors.groupingBy(AuthRelationsDO::getSourceId, Collectors.mapping(AuthRelationsDO::getTargetId, Collectors.toList())));

        // 5. 转换为MenuTreeVO
        Map<Long, AuthFunctionDO> finalFunctionMap = functionMap;
        List<MenuTreeVO> allMenuNodes = allMenus.stream().map(menu -> {
            MenuTreeVO menuVO = new MenuTreeVO();
            menuVO.setId(menu.getId());
            menuVO.setName(menu.getName());
            menuVO.setSort(menu.getSort());
            menuVO.setAssigned(true);
            menuVO.setChildMenu(new ArrayList<>());

            // 获取该菜单下的功能点
            List<Long> functionIds = menuToFunctionsMap.get(menu.getId());
            if (CollectionUtil.isNotEmpty(functionIds)) {
                List<MenuTreeVO.FunctionVO> childFunctions = functionIds.stream().map(functionId -> {
                    AuthFunctionDO functionDO = finalFunctionMap.get(functionId);
                    if (functionDO != null) {
                        MenuTreeVO.FunctionVO functionVO = new MenuTreeVO.FunctionVO();
                        functionVO.setId(functionDO.getId());
                        functionVO.setName(functionDO.getName());
                        functionVO.setAssigned(true);
                        return functionVO;
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                menuVO.setChildFunction(childFunctions);
            } else {
                menuVO.setChildFunction(new ArrayList<>());
            }

            return menuVO;
        }).collect(Collectors.toList());

        // 6. 构建树形结构
        return buildMenuTree(allMenus, allMenuNodes);
    }

    @Override
    public void menuFunctionRelations(Long functionId, Long menuId) {
        AuthRelationsDO menuRelation = new AuthRelationsDO();
        menuRelation.setSourceId(menuId);
        menuRelation.setTargetId(functionId);
        menuRelation.setType(AuthEnum.RelationType.MENU_API.name());
        menuRelation.setCreateId(SecurityUtils.getAccountId());
        authRelationsDAO.insert(menuRelation);
    }

    @Override
    public void menuCancelFunction(Long functionId, Long menuId) {
        authRelationsDAO.delete(
                new LambdaUpdateWrapper<AuthRelationsDO>()
                        .eq(AuthRelationsDO::getType, AuthEnum.RelationType.MENU_API.name())
                        .eq(AuthRelationsDO::getSourceId, menuId)
                        .in(AuthRelationsDO::getTargetId, functionId)
        );
    }

    /**
     * 递归查询某个菜单的所有子菜单ID（包括子子菜单）
     *
     * @param menuId 菜单ID
     * @return 所有子菜单ID集合
     */
    private List<Long> getAllChildMenuIds(Long menuId) {
        List<Long> result = new ArrayList<>();

        // 查询直接子菜单
        List<AuthMenuDO> childMenus = authMenuDAO.selectList(
                new BaseLambdaQueryWrapper<AuthMenuDO>()
                        .eq(AuthMenuDO::getAffiliatedMenuId, menuId)
        );

        if (CollectionUtil.isEmpty(childMenus)) {
            return result;
        }

        // 添加直接子菜单并递归查询子子菜单
        for (AuthMenuDO childMenu : childMenus) {
            result.add(childMenu.getId());
            // 递归查询子菜单的子菜单
            result.addAll(getAllChildMenuIds(childMenu.getId()));
        }

        return result;
    }

    /**
     * 递归查询某个菜单的所有父菜单ID（包括父父菜单）
     *
     * @param menuId 菜单ID
     * @return 所有父菜单ID集合
     */
    private List<Long> getAllParentMenuIds(Long menuId) {
        List<Long> result = new ArrayList<>();

        // 查询当前菜单
        AuthMenuDO menuDO = authMenuDAO.selectById(menuId);
        if (menuDO == null || menuDO.getAffiliatedMenuId() == null) {
            return result;
        }

        // 添加父菜单
        Long parentMenuId = menuDO.getAffiliatedMenuId();
        result.add(parentMenuId);

        // 递归查询父菜单的父菜单
        result.addAll(getAllParentMenuIds(parentMenuId));

        return result;
    }
}
