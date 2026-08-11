ALTER TABLE
  `store_account` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店id',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商ID',
  MODIFY COLUMN `count_pay_number` int NULL COMMENT '支付笔数' AFTER `channel_id`,
  MODIFY COLUMN `count_pay_amount` bigint NULL COMMENT '支付金额' AFTER `count_pay_number`,
  MODIFY COLUMN `count_visit_number` int NULL COMMENT '进店总数' AFTER `count_pay_amount`,
  MODIFY COLUMN `last_view_time` datetime NULL COMMENT '最后进店时间' AFTER `count_visit_number`,
  MODIFY COLUMN `last_pay_time` datetime NULL COMMENT '最后支付时间' AFTER `last_view_time`,
  MODIFY COLUMN `last_pay_amount` bigint NULL COMMENT '最后支付金额' AFTER `last_pay_time`,
  MODIFY COLUMN `relation_type` int NULL COMMENT '0:未拉黑，1已拉黑' AFTER `last_pay_amount`,
  MODIFY COLUMN `defult` int NULL COMMENT '是否默认：1 是' AFTER `relation_type`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `defult`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_store_account_store_id`(`store_id`) COMMENT '门店id',
ADD
  INDEX `auto_idx_store_account_account_id`(`account_id`) COMMENT '客户id',
ADD
  INDEX `auto_idx_store_account_channel_id`(`channel_id`) COMMENT '渠道商ID';
