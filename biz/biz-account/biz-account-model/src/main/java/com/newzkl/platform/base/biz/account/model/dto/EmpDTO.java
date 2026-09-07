package com.newzkl.platform.base.biz.account.model.dto;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 员工纯净视图
 *
 * <p>字段与 {@code emp} 表一一对应, 不含任何副数据。员工的登录凭证与主子关系已收敛到
 * {@code account} 表, {@code emp} 只留员工类型一列, 故本类字段极少。需要账号侧展示字段时
 * 走 {@link com.newzkl.platform.base.biz.account.model.res.EmpRes}</p>
 *
 * <p>不加 {@code @Builder}: 本类 {@code extends BaseRes} 取 id/createTime 等公共列,
 * Lombok builder 不含父类字段会误导调用方; 且实例统一由 {@code TransferUtils} / MapStruct 产出,
 * 不走 builder</p>
 *
 * @author KC
 * @ext 主数据 emp (无副数据)
 */
@Data
public class EmpDTO extends BaseRes {

    /**
     * 员工类型 (MANAGER 管理员 / SIMPLE 普通), JSON 出参为 code 数值
     */
    private AccountEnum.EmpType type;
}
