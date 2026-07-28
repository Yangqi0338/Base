package com.newzkl.platform.base.biz.auth.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.AuthRepository;
import com.newzkl.platform.base.biz.auth.domain.service.AuthDomain;
import com.newzkl.platform.base.biz.auth.model.rbac.req.*;
import com.newzkl.platform.base.biz.auth.model.rbac.vo.FunctionVO;
import com.newzkl.platform.base.biz.auth.model.rbac.vo.LimitRoleVO;
import com.newzkl.platform.base.biz.auth.model.rbac.vo.MenuTreeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthDomainImpl implements AuthDomain {

    private final AuthRepository authRepository;

    @Override
    public void roleCreate(RoleReq roleReq) {
        authRepository.roleCreate(roleReq);
    }

    @Override
    public void roleDelete(Long roleId) {
        authRepository.roleDelete(roleId);
    }

    @Override
    public void roleEdit(RoleReq roleReq) {
        authRepository.roleEdit(roleReq);
    }

    @Override
    public Page<LimitRoleVO> rolePage(RolePageQuery query) {
        return authRepository.rolePage(query);
    }

    @Override
    public void createFunction(FunctionReq req) {
        authRepository.createFunction(req);
    }

    @Override
    public void updateFunction(FunctionReq req) {
        authRepository.updateFunction(req);
    }

    @Override
    public void deleteFunction(Long id) {
        authRepository.deleteFunction(id);
    }

    @Override
    public Page<FunctionVO> functionPage(FunctionPageQuery query) {
        return authRepository.functionPage(query);
    }

    @Override
    public List<MenuTreeVO> functionTreeList(FunctionTreeQuery functionTreeQuery) {
        return authRepository.functionTreeList(functionTreeQuery);
    }

    @Override
    public List<FunctionVO> noEnteredFunctionList() {
        return authRepository.noEnteredFunctionList();
    }

    @Override
    public List<FunctionVO> deprecatedFunctionList() {
        return authRepository.deprecatedFunctionList();
    }

    @Override
    public void createMenu(MenuReq req) {
        authRepository.createMenu(req);
    }

    @Override
    public void updateMenu(MenuReq req) {
        authRepository.updateMenu(req);
    }

    @Override
    public void deleteMenu(Long id) {
        authRepository.deleteMenu(id);
    }

    @Override
    public void checkFunction(FunctionRelationsReq req) {
        authRepository.checkFunction(req);

    }

    @Override
    public void cancelCheckFunction(FunctionRelationsReq req) {
        authRepository.cancelCheckFunction(req);
    }

    @Override
    public List<MenuTreeVO> roleFunctionTreeList(Long roleId) {
        return authRepository.roleFunctionTreeList(roleId);
    }

    @Override
    public void menuFunctionRelations(Long functionId, Long menuId) {
        authRepository.menuFunctionRelations(functionId, menuId);
    }

    @Override
    public void menuCancelFunction(Long functionId, Long menuId) {
        authRepository.menuCancelFunction(functionId, menuId);
    }
}
