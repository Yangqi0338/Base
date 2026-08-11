ALTER TABLE
  `market_bind` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `market_id` bigint NULL COMMENT '市场id',
  MODIFY COLUMN `bind_type` bigint NULL COMMENT '绑定类型',
  MODIFY COLUMN `user_id` bigint NULL COMMENT '客户id',
  MODIFY COLUMN `user_name` varchar(255) NULL COMMENT '客户名称',
  MODIFY COLUMN `state` int NULL COMMENT '状态',
  MODIFY COLUMN `debind_time` datetime NULL COMMENT '解除绑定时间',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `debind_time`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_market_bind_market_id`(`market_id`) COMMENT '市场id',
ADD
  INDEX `auto_idx_market_bind_user_id`(`user_id`) COMMENT '客户id';
