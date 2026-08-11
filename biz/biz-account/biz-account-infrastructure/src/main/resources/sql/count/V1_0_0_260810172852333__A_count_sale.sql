ALTER TABLE
  `count_sale` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `role` varchar(255) NULL COMMENT '角色编码(旧表以字符串存储角色码)',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '账号 ID',
  MODIFY COLUMN `date` datetime NULL COMMENT '统计日期',
  MODIFY COLUMN `total_order_number` int NULL COMMENT '累计订单数',
  MODIFY COLUMN `total_order_amount` bigint NULL COMMENT '累计订单金额(Money, 落库 BIGINT 分)',
  MODIFY COLUMN `total_refund_number` int NULL COMMENT '累计退款数',
  MODIFY COLUMN `total_refund_amount` bigint NULL COMMENT '累计退款金额(Money, 落库 BIGINT 分)',
ADD
  COLUMN `contribute_amount` bigint NULL COMMENT '贡献金额(Money, 落库 BIGINT 分)',
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
  INDEX `auto_idx_count_sale_role`(`role`) COMMENT '角色编码',
ADD
  INDEX `auto_idx_count_sale_account_id`(`account_id`) COMMENT '账号 ID',
ADD
  INDEX `auto_idx_count_sale_date`(`date`) COMMENT '统计日期',
  COMMENT = '销售统计持久化对象';
