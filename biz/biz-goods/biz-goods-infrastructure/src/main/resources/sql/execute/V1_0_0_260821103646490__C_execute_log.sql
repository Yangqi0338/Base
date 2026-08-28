CREATE TABLE `execute_log` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `type` int NULL COMMENT '操作类型',
  `target_id` bigint NULL COMMENT '操作主键',
  `execute_user_id` bigint NULL COMMENT '操作人ID',
  `execute_user_name` varchar(255) NULL COMMENT '操作人名称',
  `old_data` json NULL COMMENT '原数据',
  `update_data` json NULL COMMENT '改动数据',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_execute_log_execute_user_id`(`execute_user_id`) COMMENT '操作人ID',
  INDEX `auto_idx_execute_log_target_id`(`target_id`) COMMENT '操作主键'
) COMMENT = '操作日志';
