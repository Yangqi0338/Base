ALTER TABLE
  `market_goods_relation` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `goods_id` bigint NULL COMMENT '商品id',
  MODIFY COLUMN `market_id` bigint NULL COMMENT '市场id',
  MODIFY COLUMN `relation_type` int NULL COMMENT '关系类型(1：一级市场商品  2：二级市场商品  3：市场选品商品)',
  MODIFY COLUMN `user_id` bigint NULL COMMENT '用户id(0：为平台   >0:为客户)',
  MODIFY COLUMN `sell_num` int NULL COMMENT '销量',
  MODIFY COLUMN `sell_amount` bigint NULL COMMENT '销售额',
  MODIFY COLUMN `state` int NULL COMMENT '状态(1：正常  0：删除)' AFTER `sell_amount`,
  MODIFY COLUMN `de_bind_time` datetime NULL COMMENT '解绑时间' AFTER `state`,
  MODIFY COLUMN `goods_info` json NULL COMMENT '商品信息' AFTER `discount_rate`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `goods_info`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_market_goods_relation_goods_id`(`goods_id`) COMMENT '商品id',
ADD
  INDEX `auto_idx_market_goods_relation_market_id`(`market_id`) COMMENT '市场id',
ADD
  INDEX `auto_idx_market_goods_relation_user_id`(`user_id`) COMMENT '用户id';
