package com.newzkl.platform.base.biz.auth.action.service;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.auth.domain.service.PermissionDomain;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionDTO;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
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
import java.util.List;
import java.util.Map;
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
            PermissionDTO classNode = new PermissionDTO();
            classNode.setType(PermissionEnum.Type.FUNC);
            classNode.setCode(resolveClassCode(cls));
            classNode.setName(resolveClassName(cls));
            classNode.setRoute(classRoute);
            classNode.setChildren(new ArrayList<>());

            for (HandlerMethod hm : methods) {
                Method method = hm.getMethod();
                if (!method.isAnnotationPresent(FuncPermission.class)) {
                    continue;
                }
                FuncPermission anno = method.getAnnotation(FuncPermission.class);
                PermissionDTO methodNode = new PermissionDTO();
                methodNode.setType(PermissionEnum.Type.FUNC);
                methodNode.setCode(anno.code().isEmpty() ? compactClassName(cls) + "#" + method.getName() : anno.code());
                methodNode.setName(anno.value().isEmpty() ? method.getName() : anno.value());
                methodNode.setRoute(resolveMethodRoute(hm, classRoute));
                classNode.getChildren().add(methodNode);
            }
            classNodes.add(classNode);
        }
        return permissionDomain.syncFunc(classNodes);
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
