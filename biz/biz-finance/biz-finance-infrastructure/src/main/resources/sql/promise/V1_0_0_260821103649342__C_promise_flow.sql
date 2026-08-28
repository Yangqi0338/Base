CREATE TABLE `promise_flow` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `account_id` bigint NULL COMMENT '账号ID (查询)',
  `identity` bigint NULL COMMENT '角色ID',
  `promise_pay_type` int NULL COMMENT '保证金类型',
  `amount` bigint NULL COMMENT '金额',
  `pay_type` int NULL COMMENT '支付方式',
  `certificate_url` varchar(255) NULL COMMENT '支付凭证',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_promise_flow_account_id`(`account_id`) COMMENT '账号ID (查询)',
  INDEX `auto_idx_promise_flow_identity`(`identity`) COMMENT '角色ID'
) COMMENT = '保证金流水';
