CREATE TABLE `spu_attribute` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `spu_id` bigint NULL COMMENT 'spuId(查询)',
  `type` int NULL COMMENT '类型(查询)',
  `name` varchar(255) NULL COMMENT '名称',
  `value` text NULL COMMENT '手动添加规格或参数的值，参数单值',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_spu_attribute_spu_id`(`spu_id`) COMMENT 'spuId'
) COMMENT = 'spu属性';
