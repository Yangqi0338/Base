ALTER TABLE
  `execute_log` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `execute_user_name` varchar(255) NULL COMMENT '操作人名称',
  MODIFY COLUMN `old_data` json NULL COMMENT '原数据',
  MODIFY COLUMN `update_data` json NULL COMMENT '改动数据',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `update_data`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_execute_log_target_id`(`target_id`) COMMENT '操作主键',
ADD
  INDEX `auto_idx_execute_log_execute_user_id`(`execute_user_id`) COMMENT '操作人ID';
