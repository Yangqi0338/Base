ALTER TABLE
  `store_zone_goods_relation` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `zone_code` varchar(255) NULL COMMENT '专区code',
  MODIFY COLUMN `goods_id` bigint NULL COMMENT '商品id',
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店id',
  MODIFY COLUMN `store_name` varchar(255) NULL COMMENT '门店名称',
  MODIFY COLUMN `executor` json NULL COMMENT '操作人信息',
  MODIFY COLUMN `creator_id` bigint NULL COMMENT '创建人id',
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
  MODIFY COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '门店专区商品关系领域对象';
