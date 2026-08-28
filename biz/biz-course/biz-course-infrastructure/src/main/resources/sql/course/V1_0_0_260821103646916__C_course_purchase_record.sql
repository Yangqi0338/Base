CREATE TABLE `course_purchase_record` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `order_no` bigint NULL COMMENT '订单编号(唯一)',
  `course_id` bigint NULL COMMENT '课程ID',
  `course_num` varchar(255) NULL COMMENT '课程编码(冗余)',
  `user_id` bigint NULL COMMENT '购买用户ID',
  `original_price` bigint NULL COMMENT '课程原价(单位分)',
  `pay_price` bigint NULL COMMENT '实际支付金额(单位分)',
  `pay_state` int NULL COMMENT '支付状态(0-待支付, 1-支付成功, 2-支付失败)',
  `pay_time` datetime NULL COMMENT '支付完成时间',
  `pay_type` int NULL COMMENT '支付方式(1-微信支付, 2-支付宝支付)',
  `pay_no` varchar(255) NULL COMMENT '第三方支付流水号',
  `pay_url` varchar(255) NULL COMMENT '支付链接/二维码',
  `expire_time` datetime NULL COMMENT '订单过期时间',
  `cancel_time` datetime NULL COMMENT '订单取消时间',
  `remark` varchar(255) NULL COMMENT '备注',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`)
) COMMENT = '课程购买记录(course_purchase_record)持久化对象

<p>价格单位分。</p>';
