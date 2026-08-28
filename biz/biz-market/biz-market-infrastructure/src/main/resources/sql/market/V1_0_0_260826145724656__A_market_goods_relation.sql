ALTER TABLE
  `market_goods_relation` MODIFY COLUMN `relation_type` int NULL COMMENT '关系类型(1：一级市场商品  2：二级市场商品  3：市场选品商品)[2二市场-商品,3市场选品-商品]';
