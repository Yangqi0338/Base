CREATE TABLE `market_bind` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `market_id` bigint NULL COMMENT '市场id',
  `bind_type` bigint NULL COMMENT '绑定类型',
  `user_id` bigint NULL COMMENT '客户id',
  `user_name` varchar(255) NULL COMMENT '客户名称',
  `state` int NULL COMMENT '状态',
  `debind_time` datetime NULL COMMENT '解除绑定时间',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_market_bind_market_id`(`market_id`) COMMENT '市场id',
  INDEX `auto_idx_market_bind_user_id`(`user_id`) COMMENT '客户id'
);
