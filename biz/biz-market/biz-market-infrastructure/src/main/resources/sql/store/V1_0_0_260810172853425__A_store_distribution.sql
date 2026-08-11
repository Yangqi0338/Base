ALTER TABLE
  `store_distribution` DROP COLUMN `del_state`,
  DROP COLUMN `source`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `goods_id` bigint NULL COMMENT '商品id',
  MODIFY COLUMN `sku_id` bigint NULL COMMENT 'skuId',
  MODIFY COLUMN `market_id` bigint NULL COMMENT '来源',
  MODIFY COLUMN `data_type` int NULL COMMENT '数据类型(0：商品  1：sku)',
  MODIFY COLUMN `sell_price` bigint NULL COMMENT '销售价(Money，落库 BIGINT 分)',
  MODIFY COLUMN `sell_num` int NULL COMMENT '销量',
  MODIFY COLUMN `store_id` bigint NULL COMMENT '门店id',
  MODIFY COLUMN `goods_state` int NULL COMMENT '商品状态',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商id',
  MODIFY COLUMN `unit_price` bigint NULL COMMENT '零售价(Money，落库 BIGINT 分)' AFTER `channel_id`,
  MODIFY COLUMN `supplier_price` bigint NULL COMMENT '供货价(Money，落库 BIGINT 分)' AFTER `unit_price`,
  MODIFY COLUMN `goods_info` varchar(255) NULL COMMENT '商品信息' AFTER `supplier_price`,
  MODIFY COLUMN `need_update` int NULL COMMENT '是否需要更新(0不需要，1需要)' AFTER `goods_info`,
  MODIFY COLUMN `recommendation_time` datetime NULL COMMENT '推荐时间' AFTER `need_update`,
  MODIFY COLUMN `up_time` datetime NULL COMMENT '上架时间' AFTER `recommendation_time`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `up_time`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_store_distribution_goods_id`(`goods_id`) COMMENT '商品id',
ADD
  INDEX `auto_idx_store_distribution_sku_id`(`sku_id`) COMMENT 'skuId',
ADD
  INDEX `auto_idx_store_distribution_market_id`(`market_id`) COMMENT '来源',
ADD
  INDEX `auto_idx_store_distribution_store_id`(`store_id`) COMMENT '门店id',
ADD
  INDEX `auto_idx_store_distribution_goods_state`(`goods_state`) COMMENT '商品状态',
ADD
  INDEX `auto_idx_store_distribution_channel_id`(`channel_id`) COMMENT '渠道商id';
