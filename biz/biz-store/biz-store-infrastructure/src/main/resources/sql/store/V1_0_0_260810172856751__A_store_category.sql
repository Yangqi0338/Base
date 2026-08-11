ALTER TABLE
  `store_category` DROP COLUMN `creator`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '门店名称',
  MODIFY COLUMN `logo` varchar(255) NULL COMMENT '图标',
  MODIFY COLUMN `index` int NULL COMMENT '排序',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `index`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))';
