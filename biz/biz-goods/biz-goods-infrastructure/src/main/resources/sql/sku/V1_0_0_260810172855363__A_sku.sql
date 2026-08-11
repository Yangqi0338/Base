ALTER TABLE
  `sku` DROP COLUMN `inventory`,
  DROP COLUMN `inventory_warning`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `weight` double(6, 2) NULL COMMENT '重量(千克)',
  MODIFY COLUMN `volume` double(6, 2) NULL COMMENT '体积(m3)',
  MODIFY COLUMN `spu_id` bigint NULL COMMENT 'spuId(查询)',
  MODIFY COLUMN `market_price` bigint NULL COMMENT '市场价',
  MODIFY COLUMN `supply_price` bigint NULL COMMENT '供货价',
  MODIFY COLUMN `sale_price` bigint NULL COMMENT '销售价(to channel)' AFTER `supply_price`,
  MODIFY COLUMN `unit_price` bigint NULL COMMENT '销售价(to c)' AFTER `sale_price`,
  MODIFY COLUMN `sale_attribute` json NULL COMMENT '商品销售属性(json格式)' AFTER `unit_price`,
  MODIFY COLUMN `out_sku_id` varchar(255) NULL COMMENT '外部SkuId' AFTER `sale_attribute`,
  MODIFY COLUMN `sale_price_rate` float(4, 2) NULL COMMENT '销售价加价比例' AFTER `out_sku_id`,
  MODIFY COLUMN `buy_start_qty` int NULL COMMENT '起购数量' AFTER `sale_price_rate`,
  MODIFY COLUMN `expand` json NULL COMMENT '追加: 扩展字段' AFTER `buy_start_qty`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `expand`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_sku_spu_id`(`spu_id`) COMMENT 'spuId';
