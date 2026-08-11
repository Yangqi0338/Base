CREATE TABLE `permission_relation` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `type` varchar(255) NULL COMMENT '关系类型',
  `source_id` bigint NULL COMMENT '源对象ID(account 或 role)',
  `target_id` bigint NULL COMMENT '目标对象ID(role 或 permission)',
  `source` varchar(255) NULL COMMENT '关系来源',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_idx_source`(`type`, `source_id`),
  INDEX `auto_idx_idx_target`(`type`, `target_id`),
  UNIQUE INDEX `auto_idx_idx_key`(`type`, `source_id`, `target_id`)
) COMMENT = '权限关系持久化对象

<p>account-role / role-permission / account-permission 三方多态关联单表</p>';
