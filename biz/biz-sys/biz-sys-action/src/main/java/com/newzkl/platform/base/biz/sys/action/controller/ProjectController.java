package com.newzkl.platform.base.biz.sys.action.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.domain.service.ProjectDomain;
import com.newzkl.platform.base.biz.sys.domain.service.RegionDomain;
import com.newzkl.platform.base.biz.sys.model.project.query.ProjectQuery;
import com.newzkl.platform.base.biz.sys.model.project.req.ProjectSaveReq;
import com.newzkl.platform.base.biz.sys.model.project.res.ProjectListRes;
import com.newzkl.platform.base.biz.sys.model.project.res.ProjectRes;
import com.newzkl.platform.base.biz.sys.model.region.vo.Area;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.check.UpdateCommand;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台-项目控制器
 *
 * <p>迁移说明: 源 {@code ProjectController} 用 {@code ICommonService#getBusinessRegion}
 * 回填省/市/区名称, 但调用点只设 {@code parentCode}/{@code flatten} 两参 (不传
 * operatorFilter/matchStrList/flagRemove), 即未触发跨域运营商打标分支, 语义等价于
 * 故此处改走 sys 域内区域能力,
 * 无跨域 RPC 依赖。</p>
 *
 * <p>响应壳变更: {@code queryPage} 旧返回 PageHelper {@code PageInfo<ProjectVO>}
 * ({@code list}/{@code total}/{@code pageNum}), 现返 MyBatis-Plus {@code Page<ProjectListRes>}
 * ({@code records}/{@code total}/{@code current})。<b>前端需改读法</b>, 详见 {@code queryPage} 方法注释</p>
 *
 * @author KC
 */
@RestController("sysProjectController")
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    /**
     * 顶层区域的父编码
     */
    private static final int TOP_PARENT_CODE = 0;

    private final ProjectDomain projectDomain;
    private final RegionDomain regionDomain;

    /**
     * 项目详情
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public PlatformResult<ProjectRes> detail(@PathVariable Long id) {
        ProjectRes res = projectDomain.detail(id);
        res.setProvinceName(findNameByCode(null, TOP_PARENT_CODE, res.getProvince()));
        res.setCityName(findNameByCode(null, res.getProvince(), res.getCity()));
        res.setAreaName(findNameByCode(null, res.getCity(), res.getArea()));
        return PlatformResult.success(res);
    }

    /**
     * 感兴趣
     *
     * @param id       主键
     * @param interest 0 不感兴趣 1 感兴趣
     * @return 空结果
     */
    @PostMapping("/{id}/{interest}")
    public PlatformResult<Void> interest(@PathVariable Long id, @PathVariable Integer interest) {
        projectDomain.interest(id, interest);
        return PlatformResult.success();
    }

    /**
     * 新增项目
     *
     * @param saveReq 保存请求
     * @return 新增项目主键
     */
    @PostMapping("/add")
    public PlatformResult<Long> add(@Validated @RequestBody ProjectSaveReq saveReq) {
        return PlatformResult.success(projectDomain.add(saveReq));
    }

    /**
     * 编辑项目
     *
     * @param saveReq 保存请求, id 必填
     * @return 空结果
     */
    @PutMapping("/edit")
    public PlatformResult<Void> edit(@Validated({UpdateCommand.class, Default.class})
                                     @RequestBody ProjectSaveReq saveReq) {
        projectDomain.edit(saveReq);
        return PlatformResult.success();
    }

    /**
     * 删除项目
     *
     * @param id 主键
     * @return 空结果
     */
    @DeleteMapping("/del/{id}")
    public PlatformResult<Void> del(@PathVariable Long id) {
        projectDomain.del(id);
        return PlatformResult.success();
    }

    /**
     * 查询项目列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @PostMapping("/queryList")
    public PlatformResult<List<ProjectListRes>> queryList(@RequestBody ProjectQuery query) {
        query.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(fillRegionName(projectDomain.queryList(query)));
    }

    /**
     * 查询项目分页
     *
     * <p>⚠️ 出参契约(2026-07-30 更正): 原注「现直返列表」<b>已作废</b> —— 那样把 {@code total} 丢了,
     * 与 {@code rules/Architecture.md}「{@code PageInfo}→{@code IPage/Page} 直返」矛盾。
     * 现返 MyBatis-Plus {@code Page}。</p>
     *
     * <p>相对旧契约: 旧是 PageHelper {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 现是 {@code Page}({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端(mmt-app / platform-admin)需把 {@code res.data.list} 改成 {@code res.data.records}</b></p>
     *
     * @param query 查询条件
     * @return 项目分页
     */
    @PostMapping("/queryPage")
    public PlatformResult<Page<ProjectListRes>> queryPage(@RequestBody ProjectQuery query) {
        query.setAccountId(SecurityUtils.getAccountId());
        Page<ProjectListRes> page = projectDomain.queryPage(query);
        // fillRegionName 是原地回填, 直接作用于分页对象的数据行
        fillRegionName(page.getRecords());
        return PlatformResult.success(page);
    }

    /**
     * 批量回填省/市/区名称
     *
     * @param resList 项目列表视图
     * @return 回填后的列表 (原地修改)
     */
    private List<ProjectListRes> fillRegionName(List<ProjectListRes> resList) {
        if (CollUtil.isEmpty(resList)) {
            return resList;
        }
        List<Area> areaList = regionDomain.getRegionList(null, true);
        resList.forEach(res -> {
            res.setProvinceName(findNameByCode(areaList, TOP_PARENT_CODE, res.getProvince()));
            res.setCityName(findNameByCode(areaList, res.getProvince(), res.getCity()));
            res.setAreaName(findNameByCode(areaList, res.getCity(), res.getArea()));
        });
        return resList;
    }

    /**
     * 按父编码与自身编码取区域名称
     *
     * @param areaList   已加载的区域列表, 为空时按 parentCode 现取
     * @param parentCode 父编码
     * @param code       区域编码
     * @return 区域名称, 未命中返回空串
     */
    private String findNameByCode(List<Area> areaList, Integer parentCode, Integer code) {
        if (CollUtil.isEmpty(areaList)) {
            if (code == null || parentCode == null) {
                return "";
            }
            areaList = regionDomain.getRegionList(parentCode, false);
        }
        if (code == null || parentCode == null) {
            return "";
        }
        return areaList.stream()
                .filter(it -> code.equals(it.getCode()) && parentCode.equals(it.getPid()))
                .findFirst().map(Area::getName).orElse("");
    }
}
