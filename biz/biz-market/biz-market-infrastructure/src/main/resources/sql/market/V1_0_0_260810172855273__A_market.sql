ALTER TABLE
  `market` DROP COLUMN `create_user`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `market_level` int NULL COMMENT '市场等级(1：一级 2：二级)',
  MODIFY COLUMN `market_name` varchar(255) NULL COMMENT '市场名称',
  MODIFY COLUMN `market_logo` varchar(255) NULL COMMENT '市场logo',
  MODIFY COLUMN `category_id` bigint NULL COMMENT '分类id',
  MODIFY COLUMN `goods_num` int NULL COMMENT '商品数量',
  MODIFY COLUMN `sub_bind_num` int NULL COMMENT '下级推广人数量',
  MODIFY COLUMN `sell_num` int NULL COMMENT '商品总销量',
  MODIFY COLUMN `sell_amount` bigint NULL COMMENT '总销售额',
  MODIFY COLUMN `client_id` bigint NULL COMMENT '客户id(0：平台  >0：客户)',
  MODIFY COLUMN `market_type` varchar(255) NULL COMMENT '市场类型' AFTER `client_id`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `market_type`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_market_category_id`(`category_id`) COMMENT '分类id',
ADD
  INDEX `auto_idx_market_client_id`(`client_id`) COMMENT '客户id';
