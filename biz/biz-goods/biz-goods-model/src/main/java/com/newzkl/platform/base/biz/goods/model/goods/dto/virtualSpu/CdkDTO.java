package com.newzkl.platform.base.biz.goods.model.goods.dto.virtualSpu;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
* 兑换码
* @author fang
*/
@Data
public class CdkDTO {

	/**
	 * id
	 */
	private Long id;
	/**
	 * 值
	 */
	private String value;
	/**
	 * 兑换状态 0 未兑换 1 已兑换
	 */
	private Integer useState;
	/**
	 * 获取方式 0 发放 1 购买
	 */
	private Integer getType;
	/**
	 * 分配状态 0 未分配 1 运营商已分配 2 交易师已分配  3 平台已分配
	 */
	private Integer toState;
	/**
	 * 兑换时间
	 */
	private LocalDateTime useTime;
	/**
	 * 使用者ID
	 */
	private Long useId;
	/**
	 * 归属人角色
	 */
    private RoleEnum.CompanyRole belowRole;
	private Long operatorId;
	private Long dealerId;
	private Long channelId;
	private LocalDateTime toOperatorTime;
	private LocalDateTime toDealerTime;
	private LocalDateTime toChannelTime;
	/**
	 * 系统类型
	 * STORE(0,"数字门店"),
	 * HOUSE(1,"白链马"),
	 */
	private Integer systemType;
	/**
	 * 使用类型 0 用户使用 1 平台使用
	 */
	private Integer useType;
	/**
	 * 订单ID
	 */
	private Long orderId;
	/**
	 * 关联应用ID
	 */
	private Integer refAppId;
	private String useDesc;
}