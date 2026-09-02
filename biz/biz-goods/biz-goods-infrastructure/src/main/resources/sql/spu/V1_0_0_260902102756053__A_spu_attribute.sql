ALTER TABLE
  `spu_attribute` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `spu_id` bigint NULL COMMENT 'spuId(查询)',
  MODIFY COLUMN `type` int NULL COMMENT '类型(查询)[0销售属性,1参数属性]',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '名称',
  MODIFY COLUMN `value` text NULL COMMENT '手动添加规格或参数的值，参数单值',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = 'spu属性';
