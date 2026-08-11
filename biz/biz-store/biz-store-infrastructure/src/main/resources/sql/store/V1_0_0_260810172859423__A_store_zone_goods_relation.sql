ALTER TABLE
  `store_zone_goods_relation` DROP COLUMN `deleted`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `zone_code` varchar(255) NULL COMMENT '专区code',
  MODIFY COLUMN `goods_id` bigint NULL COMMENT '商品id',
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店id',
  MODIFY COLUMN `store_name` varchar(255) NULL COMMENT '门店名称',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `store_name`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_store_zone_goods_relation_zone_code`(`zone_code`) COMMENT '专区code',
ADD
  INDEX `auto_idx_store_zone_goods_relation_goods_id`(`goods_id`) COMMENT '商品id',
ADD
  INDEX `auto_idx_store_zone_goods_relation_store_id`(`store_id`) COMMENT '门店id',
  COMMENT = '门店专区商品关系领域对象';
