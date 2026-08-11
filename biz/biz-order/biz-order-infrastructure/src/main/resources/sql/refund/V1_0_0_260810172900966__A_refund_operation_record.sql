ALTER TABLE
  `refund_operation_record` MODIFY COLUMN `refund_id` bigint NULL COMMENT '售后单refund表的主键',
  MODIFY COLUMN `operator_role_code` bigint NULL COMMENT '操作方角色编码',
  MODIFY COLUMN `operator_client` varchar(255) NULL COMMENT '操作方客户端类型',
  MODIFY COLUMN `operator_name` varchar(255) NULL COMMENT '操作方名称',
  MODIFY COLUMN `after_state` int NULL COMMENT '操作后售后单状态',
  MODIFY COLUMN `operation_type` int NULL COMMENT '操作类型',
  MODIFY COLUMN `operation_content` varchar(255) NULL COMMENT '操作内容描述',
  MODIFY COLUMN `refund_amount` bigint NULL COMMENT '本次操作涉及的退款金额(落库 BIGINT 分, 与 refund 表口径一致)',
  MODIFY COLUMN `freight_company_name` varchar(255) NULL COMMENT '物流公司名称',
  MODIFY COLUMN `freight_no` varchar(255) NULL COMMENT '物流单号',
  MODIFY COLUMN `reason` varchar(255) NULL COMMENT '操作原因/备注',
  MODIFY COLUMN `ext` json NULL COMMENT '拓展字段' AFTER `reason`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `ext`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '售后操作记录表 DO';
