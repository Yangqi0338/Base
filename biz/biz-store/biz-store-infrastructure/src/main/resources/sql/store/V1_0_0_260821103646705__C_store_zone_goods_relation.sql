CREATE TABLE `store_zone_goods_relation` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `zone_code` varchar(255) NULL COMMENT '专区code',
  `goods_id` bigint NULL COMMENT '商品id',
  `store_id` bigint NULL COMMENT '门店id',
  `store_name` varchar(255) NULL COMMENT '门店名称',
  `executor` json NULL COMMENT '操作人信息',
  `creator_id` bigint NULL COMMENT '创建人id',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  PRIMARY KEY (`id`),
  INDEX `auto_idx_store_zone_goods_relation_goods_id`(`goods_id`) COMMENT '商品id',
  INDEX `auto_idx_store_zone_goods_relation_store_id`(`store_id`) COMMENT '门店id',
  INDEX `auto_idx_store_zone_goods_relation_zone_code`(`zone_code`) COMMENT '专区code'
) COMMENT = '门店专区商品关系领域对象';
