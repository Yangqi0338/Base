ALTER TABLE
  `dict_item` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `dict_id` bigint NULL COMMENT '父字典 id',
  MODIFY COLUMN `item_key` varchar(255) NULL COMMENT '条目键',
  MODIFY COLUMN `item_value` varchar(255) NULL COMMENT '条目值',
  MODIFY COLUMN `sort` int NULL COMMENT '排序(升序)',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '字典条目数据对象';
