package com.newzkl.platform.base.biz.account.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.newzkl.platform.base.biz.account.model.merchant.vo.WxMpConfigVO;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 商户持久化对象
 *
 * <p>对应旧表 {@code merchant} (旧 {@code com.zkl.scm.user.infrastructure.entity.MerchantDO})。
 * 主键与账号 ID 同值 (商户注册时以 accountId 写入), 故写入走 insert 而非自动主键。
 * 旧实现的 {@code wx_mp_config} 列以 String 存 JSON 并由业务手工序列化,
 * 本仓改为 {@code JacksonTypeHandler} 自动映射 (同 {@code LevelDO} 范式, 需
 * {@code @TableName(autoResultMap = true)})。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "merchant", autoResultMap = true)
public class MerchantDO extends BaseDO {

    /**
     * 名称
     */
    @Index
    private String name;

    /**
     * 账号名称 (手机号)
     */
    @Index
    private String username;

    /**
     * 营业执照
     */
    private String license;

    /**
     * 数字门店权限: 0 无 1 有
     */
    private Integer storePermission;

    /**
     * 微信公众号配置 (JSON 列)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private WxMpConfigVO wxMpConfig;

    /**
     * 渠道商 ID
     */
    @Index
    private Long channelId;
}
