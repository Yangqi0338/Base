ALTER TABLE
  `dict` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `code` bigint NULL COMMENT '字典业务键

<p>稳定业务键, 对应 DictEnum.Key 写死码值, 与物理主键 id 解耦</p>',
  MODIFY COLUMN `value` json NULL COMMENT '字典值',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '字典数据对象';
