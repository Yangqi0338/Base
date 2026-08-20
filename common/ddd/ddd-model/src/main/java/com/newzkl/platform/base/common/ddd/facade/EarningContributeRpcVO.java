package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 收益贡献结果
 *
 * <p>迁移: 跨域 finance 结构
 * {@code com.zkl.scm.finance.rpc.model.earnings.vo.EarningContributeRpcVO}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class EarningContributeRpcVO implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 资金账户类型
     */
    private PurseEnum.User accountType;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 上级账户ID
     */
    private Long parentId;

    /**
     * 累计消费 (分)
     */

    /**
     * 收益贡献 (分)
     */
    private Integer earningContribute;

    /**
     * 服务费贡献 (分)
     */
    private Integer serviceChangeContribute;
}
