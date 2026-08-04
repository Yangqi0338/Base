package com.newzkl.platform.base.common.ddd.domain.utils;



import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * C端订单状态机（适配三方回调场景）：仅通过当前状态和目标状态判断转换合法性
 */
@Slf4j
public class COrderStateMachine {

	// C端涉及的状态（来自OrderEnum.State）
	// 0: NEW（新订单）
	// 1: MEMBER_WAIT_PAY（C端待付款）
	// 6: WAIT_DELIVERY（待发货）
	// 8: WAIT_RECEIVE（待收货）
	// 10: DOWN_RECEIVE（已收货）
	// 12: SUCCESS（已完成）
	// 99: CLOSE（已关闭）

	/**
	 * 核心转换规则：当前状态 → 允许的目标状态集合 键：当前状态；值：该状态可合法转换到的目标状态集合
	 */
	private static final Map<OrderEnum.State, Set<OrderEnum.State>> LEGAL_TRANSITIONS;

	static {
		LEGAL_TRANSITIONS = new HashMap<>();

		// 1. 新订单（NEW, 0）允许转换到：待付款、关闭
		Set<OrderEnum.State> newStates = new HashSet<>();
		newStates.add(OrderEnum.State.MEMBER_WAIT_PAY); // 0→1
		newStates.add(OrderEnum.State.CLOSE); // 0→99
		LEGAL_TRANSITIONS.put(OrderEnum.State.NEW, Collections.unmodifiableSet(newStates));

		// 2. C端待付款（MEMBER_WAIT_PAY, 1）允许转换到：待发货、关闭
		Set<OrderEnum.State> waitPayStates = new HashSet<>();
		waitPayStates.add(OrderEnum.State.WAIT_DELIVERY); // 1→6
		waitPayStates.add(OrderEnum.State.CLOSE); // 1→99
		LEGAL_TRANSITIONS.put(OrderEnum.State.MEMBER_WAIT_PAY, Collections.unmodifiableSet(waitPayStates));

		// 3. 待发货（WAIT_DELIVERY, 6）允许转换到：待收货、关闭
		Set<OrderEnum.State> waitDeliveryStates = new HashSet<>();
		waitDeliveryStates.add(OrderEnum.State.WAIT_RECEIVE); // 6→8
		waitDeliveryStates.add(OrderEnum.State.CLOSE); // 6→99
		LEGAL_TRANSITIONS.put(OrderEnum.State.WAIT_DELIVERY, Collections.unmodifiableSet(waitDeliveryStates));

		// 4. 待收货（WAIT_RECEIVE, 8）允许转换到：已收货、关闭
		Set<OrderEnum.State> waitReceiveStates = new HashSet<>();
		waitReceiveStates.add(OrderEnum.State.DOWN_RECEIVE); // 8→10
		waitReceiveStates.add(OrderEnum.State.CLOSE); // 8→99
		LEGAL_TRANSITIONS.put(OrderEnum.State.WAIT_RECEIVE, Collections.unmodifiableSet(waitReceiveStates));

		// 5. 已收货（DOWN_RECEIVE, 10）允许转换到：已完成、关闭
		Set<OrderEnum.State> downReceiveStates = new HashSet<>();
		downReceiveStates.add(OrderEnum.State.SUCCESS); // 10→12
		downReceiveStates.add(OrderEnum.State.CLOSE); // 10→99
		LEGAL_TRANSITIONS.put(OrderEnum.State.DOWN_RECEIVE, Collections.unmodifiableSet(downReceiveStates));

		// 6. 已完成（SUCCESS, 12）：终态，不允许转换到任何状态
		LEGAL_TRANSITIONS.put(OrderEnum.State.SUCCESS, Collections.emptySet());

		// 7. 已关闭（CLOSE, 99）：终态，不允许转换到任何状态
		LEGAL_TRANSITIONS.put(OrderEnum.State.CLOSE, Collections.emptySet());
	}

	/**
	 * 校验C端订单状态转换是否合法
	 * 
	 * @param currentState
	 *            当前状态（必须是C端涉及的状态）
	 * @param targetState
	 *            目标状态（必须是C端涉及的状态）
	 * @return 合法返回true；不合法抛出异常
	 * @throws IllegalArgumentException
	 *             当状态不合法或转换不被允许时抛出
	 */
	public static boolean validateTransition(OrderEnum.State currentState, OrderEnum.State targetState) {
		// 1. 校验当前状态和目标状态是否为C端合法状态
		if (!isCState(currentState)) {
			throw new IllegalArgumentException("当前状态[" + getStateDesc(currentState) + "]不属于C端订单状态");
		}
		if (!isCState(targetState)) {
			throw new IllegalArgumentException("目标状态[" + getStateDesc(targetState) + "]不属于C端订单状态");
		}

		// 2. 校验是否为同一状态（无需转换）
		if (currentState == targetState) {
			return true; // 同一状态视为合法（可能是重复回调）
		}

		// 3. 校验转换是否在合法规则内
		Set<OrderEnum.State> allowedTargets = LEGAL_TRANSITIONS.get(currentState);
		if (allowedTargets == null || !allowedTargets.contains(targetState)) {
			throw new IllegalArgumentException(String.format("C端订单不支持的状态转换：当前状态[%s] → 目标状态[%s]",
					getStateDesc(currentState), getStateDesc(targetState)));
		}

		return true;
	}

	/**
	 * 执行状态转换（校验通过后返回目标状态）
	 * 
	 * @param currentState
	 *            当前状态
	 * @param targetState
	 *            目标状态
	 * @return 目标状态（校验通过后）
	 */
	public static OrderEnum.State transition(OrderEnum.State currentState, OrderEnum.State targetState) {
		// 先校验转换合法性
		if (validateTransition(currentState, targetState)) {
			// 若为同一状态，直接返回（避免日志冗余）
			if (currentState != targetState) {
                // TODO
//                log.info("C端订单状态转换成功：{} → {}", getStateDesc(currentState), getStateDesc(targetState));
			}
			return targetState;
		}
		// 若校验不通过，validateTransition已抛出异常，此处不会执行
		return currentState;
	}

	// 辅助方法：获取状态描述（含code和info）
	private static String getStateDesc(OrderEnum.State state) {
		if (state == null) {
			return "null";
		}
		return state.getValue() + "(" + state.getCode() + ")";
	}

	/**
	 * 校验状态是否为C端涉及的状态
	 */
	private static boolean isCState(OrderEnum.State state) {
		if (state == null) {
			return false;
		}
		int code = state.getCode();
		// C端涉及的状态码：0、1、6、8、10、12、99
		return code == 0 || code == 1 || code == 6 || code == 8 || code == 10 || code == 12 || code == 99;
	}

	// 测试方法（模拟三方回调场景）
	public static void main(String[] args) {
		// 测试1：合法转换流程
		try {
			OrderEnum.State current = OrderEnum.State.NEW;
			OrderEnum.State target = OrderEnum.State.MEMBER_WAIT_PAY;
			transition(current, target); // 0→1（合法）

			current = target;
			target = OrderEnum.State.WAIT_DELIVERY;
			transition(current, target); // 1→6（合法）

			current = target;
			target = OrderEnum.State.WAIT_RECEIVE;
			transition(current, target); // 6→8（合法）

			current = target;
			target = OrderEnum.State.DOWN_RECEIVE;
			transition(current, target); // 8→10（合法）

			current = target;
			target = OrderEnum.State.SUCCESS;
			transition(current, target); // 10→12（合法）
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 测试2：中途关闭（待发货→关闭）
		try {
			OrderEnum.State current = OrderEnum.State.WAIT_DELIVERY;
			OrderEnum.State target = OrderEnum.State.CLOSE;
			transition(current, target); // 6→99（合法）
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 测试3：非法转换（新订单→待收货）
		try {
			OrderEnum.State current = OrderEnum.State.NEW;
			OrderEnum.State target = OrderEnum.State.WAIT_RECEIVE;
			transition(current, target); // 0→8（非法）
		} catch (IllegalArgumentException e) {
			System.out.println("非法转换测试结果：" + e.getMessage());
		}

		// 测试4：终态转换（已完成→任何状态）
		try {
			OrderEnum.State current = OrderEnum.State.SUCCESS;
			OrderEnum.State target = OrderEnum.State.CLOSE;
			transition(current, target); // 12→99（非法）
		} catch (IllegalArgumentException e) {
			System.out.println("终态转换测试结果：" + e.getMessage());
		}

		// 测试5：同一状态（重复回调）
		try {
			OrderEnum.State current = OrderEnum.State.WAIT_RECEIVE;
			OrderEnum.State target = OrderEnum.State.WAIT_RECEIVE;
			transition(current, target); // 8→8（合法，无日志）
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}