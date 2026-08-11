ALTER TABLE
  `earning_contribute` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `account_type` int NULL COMMENT '客户类型',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT '客户姓名',
  MODIFY COLUMN `parent_id` bigint NULL COMMENT '直推上级id',
ADD
  COLUMN `contribute_id` bigint NULL COMMENT '贡献人' AFTER `parent_id`,
  MODIFY COLUMN `total_consume` bigint NULL COMMENT '总消费',
  MODIFY COLUMN `earning_contribute` bigint NULL COMMENT '分润贡献',
  MODIFY COLUMN `service_change_contribute` bigint NULL COMMENT '服务费贡献',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息',
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id',
ADD
  COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_earning_contribute_account_id`(`account_id`) COMMENT '客户id',
ADD
  INDEX `auto_idx_earning_contribute_account_type`(`account_type`) COMMENT '客户类型',
ADD
  INDEX `auto_idx_earning_contribute_parent_id`(`parent_id`) COMMENT '直推上级id',
ADD
  INDEX `auto_idx_earning_contribute_contribute_id`(`contribute_id`) COMMENT '贡献人';
