ALTER TABLE
  `promise_flow` DROP COLUMN `role_id`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
ADD
  COLUMN `role` bigint NULL COMMENT '角色ID' AFTER `account_id`,
  MODIFY COLUMN `promise_pay_type` int NULL COMMENT '保证金类型',
  MODIFY COLUMN `amount` bigint NULL COMMENT '金额',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `certificate_url`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_promise_flow_account_id`(`account_id`) COMMENT '账号ID (查询)',
ADD
  INDEX `auto_idx_promise_flow_role`(`role`) COMMENT '角色ID';
