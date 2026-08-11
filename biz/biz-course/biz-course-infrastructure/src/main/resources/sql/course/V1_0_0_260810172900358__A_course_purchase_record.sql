ALTER TABLE
  `course_purchase_record` DROP COLUMN `is_deleted`,
  DROP COLUMN `create_by`,
  DROP COLUMN `update_by`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `order_no` bigint NULL COMMENT '订单编号(唯一)',
  MODIFY COLUMN `course_id` bigint NULL COMMENT '课程ID',
  MODIFY COLUMN `course_num` varchar(255) NULL COMMENT '课程编码(冗余)',
  MODIFY COLUMN `user_id` bigint NULL COMMENT '购买用户ID',
  MODIFY COLUMN `original_price` bigint NULL COMMENT '课程原价(单位分)',
  MODIFY COLUMN `pay_price` bigint NULL COMMENT '实际支付金额(单位分)',
  MODIFY COLUMN `pay_state` int NULL COMMENT '支付状态(0-待支付, 1-支付成功, 2-支付失败)',
  MODIFY COLUMN `pay_time` datetime NULL COMMENT '支付完成时间',
  MODIFY COLUMN `pay_type` int NULL COMMENT '支付方式(1-微信支付, 2-支付宝支付)',
  MODIFY COLUMN `pay_no` varchar(255) NULL COMMENT '第三方支付流水号',
  MODIFY COLUMN `pay_url` varchar(255) NULL COMMENT '支付链接/二维码',
  MODIFY COLUMN `expire_time` datetime NULL COMMENT '订单过期时间',
  MODIFY COLUMN `cancel_time` datetime NULL COMMENT '订单取消时间',
  MODIFY COLUMN `remark` varchar(255) NULL COMMENT '备注',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `remark`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '课程购买记录(course_purchase_record)持久化对象

<p>价格单位分。</p>';
