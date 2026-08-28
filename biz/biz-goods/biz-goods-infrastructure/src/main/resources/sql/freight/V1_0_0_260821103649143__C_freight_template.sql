CREATE TABLE `freight_template` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(255) NULL COMMENT '名称(查询)',
  `free_post` int NULL COMMENT '是否包邮(0 不包邮，1 包邮)',
  `pricing_manner` int NULL COMMENT '计价方式(1 按件数，2 按重量，3 按体积)',
  `is_free_post_condition` int NULL COMMENT '是否指定条件包邮(0 否，1 是)',
  `is_default` int NULL COMMENT '是否默认模板',
  `free_post_condition` json NULL COMMENT '包邮条件',
  `region_spec` json NULL COMMENT '地区运费规则',
  `account_id` bigint NULL COMMENT '账号ID(查询)',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_freight_template_account_id`(`account_id`) COMMENT '账号ID',
  INDEX `auto_idx_freight_template_name`(`name`) COMMENT '名称'
) COMMENT = '运费模板';
