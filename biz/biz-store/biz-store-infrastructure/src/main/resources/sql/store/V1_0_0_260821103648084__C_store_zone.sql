CREATE TABLE `store_zone` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `zone_code` varchar(255) NULL COMMENT '专区code',
  `zone_name` varchar(255) NULL COMMENT '专区名称',
  `zone_subtitle` varchar(255) NULL COMMENT '专区副标题',
  `zone_describe` varchar(255) NULL COMMENT '描述',
  `state` int NULL COMMENT '状态：0 禁用,1 启用',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_store_zone_zone_code`(`zone_code`) COMMENT '专区code'
) COMMENT = '门店专区领域对象';
