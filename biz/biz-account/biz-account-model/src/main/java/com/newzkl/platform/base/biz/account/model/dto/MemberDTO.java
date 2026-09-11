package com.newzkl.platform.base.biz.account.model.dto;

import com.newzkl.platform.base.common.ddd.model.enums.account.PersonalEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDate;

/**
 * 会员纯净视图
 *
 * <p>字段与 {@code member} 表一一对应, 不含任何副数据。用于只需会员自有列的场景
 * (取生日/常住地/微信绑定等), 相比 {@link com.newzkl.platform.base.biz.account.model.res.MemberRes}
 * 少一次 account 查询</p>
 *
 * <p>不加 {@code @Builder}: 本类 {@code extends BaseRes} 取 id/createTime 等公共列,
 * Lombok builder 不含父类字段会误导调用方; 且实例统一由 {@code TransferUtils} / MapStruct 产出,
 * 不走 builder</p>
 *
 * @author KC
 * @ext 主数据 member (无副数据)
 */
@Data
public class MemberDTO extends BaseRes {

    /**
     * 用户名称
     */
    private String name;
    /**
     * 背景图
     */
    private String backgroundImg;
    /**
     * 性别 (MALE 男 / FEMALE 女), JSON 出参为 code 数值
     */
    private PersonalEnum.Gender gender;
    /**
     * 生日
     */
    private LocalDate birthday;
    /**
     * 常住地
     *
     * @ext 省份, 城市, 区县, 逗号分隔单列存储
     */
    private String residence;
    /**
     * 微信ID
     */
    private String wxId;
    /**
     * openId
     */
    private String openId;
    /**
     * unionId
     */
    private String unionId;
    /**
     * 渠道商ID
     */
    private Long channelId;
}
