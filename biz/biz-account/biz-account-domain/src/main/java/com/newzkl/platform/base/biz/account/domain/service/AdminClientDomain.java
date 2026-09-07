package com.newzkl.platform.base.biz.account.domain.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.exception.EasyExcelErrorVO;
import com.newzkl.platform.base.biz.account.model.dto.EmpDTO;
import com.newzkl.platform.base.biz.account.model.req.EmpCreateReq;
import com.newzkl.platform.base.biz.account.model.req.EmpQuery;
import com.newzkl.platform.base.biz.account.model.req.EmpSaveCommand;
import com.newzkl.platform.base.biz.account.model.res.EmpRes;

import java.io.InputStream;
import java.util.List;

/**
 * 平台端 (员工) 领域服务
 *
 * @author fang
 */
public interface AdminClientDomain {

    /**
     * 批量新增员工
     *
     * <p>旧 {@code emp} 表自带 username/password/account_id, 一行即一员工;
     * Base 拆成 {@code account} (凭证 + 主子关系) 与 {@code emp} (类型 + 岗位) 两行同主键,
     * 故一次新增写两表</p>
     *
     * @param empCreateReqList 员工新增请求列表
     * @param parentAccountId  主账号 ID
     */
    void batchEmpCreate(List<EmpCreateReq> empCreateReqList, Long parentAccountId);

    /**
     * 员工修改
     *
     * <p>除 {@code type} 外的入参字段(昵称/真实姓名/手机号/头像/父id)均已随建模收敛到 account 表,
     * 命中时先落账号侧再写 emp 行, 同事务保证两表一致。emp 表只有 {@code type} 一列</p>
     *
     * @param id      员工账号ID (与 emp 主键同值)
     * @param command 员工修改入参
     * @return 影响行数
     * @ext 主数据 emp, 副数据 account(单副, 副数据不再向下关联)
     */
    int empEdit(Long id, EmpSaveCommand command);

    /**
     * 员工纯净详情
     *
     * <p>只查 emp 一张表, 相比 {@link #empDetail} 少一次 account 查询。
     * 适用于只判员工类型(管理员/普通)的场景</p>
     *
     * @param empId 员工账号ID
     * @return 员工纯净视图
     * @ext 主数据 emp (无副数据)
     */
    EmpDTO empBase(Long empId);

    /**
     * 员工聚合详情
     *
     * <p>主数据 emp 由 assembler 搬列, 副数据 account 逐字段显式填充</p>
     *
     * @param empId 员工账号ID
     * @return 员工聚合视图
     * @ext 主数据 emp, 副数据 account(单副, 副数据不再向下关联)。与
     *      {@code AccountController.identityDetail} 的「主 account / 副身份」方向相反, 两者不可互相替代
     */
    EmpRes empDetail(Long empId);
}
