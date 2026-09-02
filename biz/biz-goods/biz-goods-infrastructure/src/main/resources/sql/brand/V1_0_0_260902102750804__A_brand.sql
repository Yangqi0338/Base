ALTER TABLE
  `brand` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `account_id` bigint NULL COMMENT '用户ID',
  MODIFY COLUMN `name` varchar(255) NULL COMMENT '名称(查询)',
  MODIFY COLUMN `logo` varchar(255) NULL COMMENT '图标',
  MODIFY COLUMN `state` varchar(8) NULL COMMENT '状态[PENDING待审核,APPROVED已通过,REJECTED已拒绝]',
  MODIFY COLUMN `category_id_list` varchar(255) NULL COMMENT '类目ID',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '品牌';
