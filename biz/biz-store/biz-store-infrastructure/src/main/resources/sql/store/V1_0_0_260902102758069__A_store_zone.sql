ALTER TABLE
  `store_zone` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `zone_code` varchar(255) NULL COMMENT '专区code',
  MODIFY COLUMN `zone_name` varchar(255) NULL COMMENT '专区名称',
  MODIFY COLUMN `zone_subtitle` varchar(255) NULL COMMENT '专区副标题',
  MODIFY COLUMN `zone_describe` varchar(255) NULL COMMENT '描述',
  MODIFY COLUMN `state` int NULL COMMENT '状态：0 禁用,1 启用',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '门店专区领域对象';
