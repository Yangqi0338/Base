package com.newzkl.platform.base.biz.auth.action.service;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.auth.domain.service.PermissionDomain;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionDTO;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能权限扫包同步
 *
 * <p>扫描所有标注 {@code FuncPermission} 的 Controller 类与方法, 构建类节点树 (类为父, 方法为子),
 * 交由领域层按 code upsert FUNC 节点、软删孤儿并重算受影响账号, 属入口层胶水 (依赖 web-mvc 与 ddd-action)</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionSyncService {

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    private final PermissionDomain permissionDomain;

    /**
     * 执行功能权限同步
     *
     * @return 同步结果统计
     */
    public PermissionDomain.SyncResult sync() {
        Map<Class<?>, List<HandlerMethod>> classMethods = handlerMapping.getHandlerMethods().values().stream()
                .collect(Collectors.groupingBy(HandlerMethod::getBeanType));

        List<PermissionDTO> classNodes = new ArrayList<>();
        for (Map.Entry<Class<?>, List<HandlerMethod>> e : classMethods.entrySet()) {
            Class<?> cls = e.getKey();
            List<HandlerMethod> methods = e.getValue();
            boolean classHas = cls.isAnnotationPresent(FuncPermission.class);
            boolean anyMethodHas = methods.stream()
                    .anyMatch(hm -> hm.getMethod().isAnnotationPresent(FuncPermission.class));
            if (!classHas && !anyMethodHas) {
                continue;
            }

            String classRoute = resolveClassRoute(cls);
            List<HandlerMethod> annotatedMethods = methods.stream()
                    .filter(hm -> hm.getMethod().isAnnotationPresent(FuncPermission.class))
                    .toList();

            // 端展开: 父节点端 = 自身端 ∪ 所有子方法端, 保证任一子方法所属端都有父节点; 每个端生成一棵独立树
            Set<AccountEnum.Client> classClients = resolveClients(cls, null);
            Set<AccountEnum.Client> allClients = new LinkedHashSet<>(classClients);
            for (HandlerMethod hm : annotatedMethods) {
                allClients.addAll(resolveClients(cls, hm.getMethod()));
            }

            for (AccountEnum.Client client : allClients) {
                PermissionDTO classNode = new PermissionDTO();
                classNode.setClient(client);
                classNode.setType(PermissionEnum.Type.FUNC);
                classNode.setCode(resolveClassCode(cls));
                classNode.setName(resolveClassName(cls));
                classNode.setRoute(classRoute);
                classNode.setChildren(new ArrayList<>());

                for (HandlerMethod hm : annotatedMethods) {
                    Method method = hm.getMethod();
                    if (!resolveClients(cls, method).contains(client)) {
                        continue;
                    }
                    FuncPermission anno = method.getAnnotation(FuncPermission.class);
                    PermissionDTO methodNode = new PermissionDTO();
                    methodNode.setClient(client);
                    methodNode.setType(PermissionEnum.Type.FUNC);
                    methodNode.setCode(anno.code().isEmpty() ? compactClassName(cls) + "#" + method.getName() : anno.code());
                    methodNode.setName(anno.value().isEmpty() ? method.getName() : anno.value());
                    methodNode.setRoute(resolveMethodRoute(hm, classRoute));
                    classNode.getChildren().add(methodNode);
                }
                classNodes.add(classNode);
            }
        }
        return permissionDomain.syncFunc(classNodes);
    }

    /**
     * 解析目标元素所属端集合
     *
     * <p>优先级: FuncPermission 显式指定端 → 用之; 否则若有 RoleLimit, 取 roleLimit.client(),
     * roleLimit 无 client 则取 roleLimit.value() 各身份归属端去重; 均无则默认平台端 ADMIN。</p>
     *
     * @param cls    目标类
     * @param method 目标方法, 为 null 时解析类级
     * @return 端集合, 保序去重, 至少含一个端
     */
    private Set<AccountEnum.Client> resolveClients(Class<?> cls, Method method) {
        FuncPermission fp = method != null
                ? method.getAnnotation(FuncPermission.class)
                : cls.getAnnotation(FuncPermission.class);
        AccountEnum.Client[] fpClients = fp == null ? new AccountEnum.Client[0] : fp.client();
        // FuncPermission 显式指定端(非默认单 ADMIN)则直接采用
        if (!isDefaultClient(fpClients)) {
            return new LinkedHashSet<>(Arrays.asList(fpClients));
        }

        RoleLimit rl = method != null
                ? method.getAnnotation(RoleLimit.class)
                : cls.getAnnotation(RoleLimit.class);
        if (rl != null) {
            if (rl.client().length > 0) {
                return new LinkedHashSet<>(Arrays.asList(rl.client()));
            }
            if (rl.value().length > 0) {
                Set<AccountEnum.Client> derived = Arrays.stream(rl.value())
                        .map(AccountEnum.Identity::getClient)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                if (!derived.isEmpty()) {
                    return derived;
                }
            }
        }
        return new LinkedHashSet<>(List.of(AccountEnum.Client.ADMIN));
    }

    /**
     * 判断端数组是否为 FuncPermission 默认值(单元素 ADMIN), 默认视为未显式指定
     *
     * @param clients 端数组
     * @return 是否为默认端
     */
    private boolean isDefaultClient(AccountEnum.Client[] clients) {
        return clients.length == 1 && clients[0] == AccountEnum.Client.ADMIN;
    }

    private String resolveClassCode(Class<?> cls) {
        FuncPermission a = cls.getAnnotation(FuncPermission.class);
        if (a != null && !a.code().isEmpty()) {
            return a.code();
        }
        return compactClassName(cls);
    }

    private String compactClassName(Class<?> cls) {
        return cls.getName()
                .replaceFirst("^com\\.newzkl\\.platform\\.base\\.biz\\.", "")
                .replaceFirst("\\.action\\.controller\\.", ".");
    }

    private String resolveClassName(Class<?> cls) {
        FuncPermission a = cls.getAnnotation(FuncPermission.class);
        if (a != null && !a.value().isEmpty()) {
            return a.value();
        }
        try {
            String javadoc = CommonUtil.findJavaClass(cls).getComment();
            if (StrUtil.isNotBlank(javadoc)) {
                return javadoc.trim();
            }
        } catch (Exception ignore) {
            log.warn("javadoc 缺失, 回退 simpleName: {}", cls.getName());
        }
        return cls.getSimpleName();
    }

    private String resolveClassRoute(Class<?> cls) {
        RequestMapping rm = cls.getAnnotation(RequestMapping.class);
        if (rm == null || rm.value().length == 0) {
            return "";
        }
        return rm.value()[0];
    }

    private String resolveMethodRoute(HandlerMethod hm, String classPrefix) {
        RequestMapping rm = hm.getMethod().getAnnotation(RequestMapping.class);
        String methodPath = "";
        if (rm != null && rm.value().length > 0) {
            methodPath = rm.value()[0];
        } else {
            for (Annotation a : hm.getMethod().getAnnotations()) {
                try {
                    Method valueMethod = a.annotationType().getMethod("value");
                    Object v = valueMethod.invoke(a);
                    if (v instanceof String[] arr && arr.length > 0) {
                        methodPath = arr[0];
                        break;
                    }
                } catch (Exception ignore) {
                    // skip non-path annotations
                }
            }
        }
        return joinPath(classPrefix, methodPath);
    }

    private String joinPath(String a, String b) {
        if (StrUtil.isBlank(a)) {
            return b;
        }
        if (StrUtil.isBlank(b)) {
            return a;
        }
        if (a.endsWith("/") && b.startsWith("/")) {
            return a + b.substring(1);
        }
        if (!a.endsWith("/") && !b.startsWith("/")) {
            return a + "/" + b;
        }
        return a + b;
    }
}
