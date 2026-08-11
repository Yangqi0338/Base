ALTER TABLE
  `store` DROP COLUMN `merchant_id`,
  DROP COLUMN `template_id`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `logo` varchar(255) NULL COMMENT '门店logo',
  MODIFY COLUMN `longitude` double(6, 2) NULL COMMENT '经度',
  MODIFY COLUMN `latitude` double(6, 2) NULL COMMENT '纬度',
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商ID',
  MODIFY COLUMN `model_shop_id` bigint NULL COMMENT '样板店ID' AFTER `manager_id`,
  MODIFY COLUMN `is_model_shop` int NULL COMMENT '是否是样板店' AFTER `model_shop_id`,
  MODIFY COLUMN `selection_number` int NULL COMMENT '选品数量',
  MODIFY COLUMN `custom_number` int NULL COMMENT '自营商品数量',
  MODIFY COLUMN `dealer_number` int NULL COMMENT '成交笔数',
  MODIFY COLUMN `dealer_amount` bigint NULL COMMENT '成交金额',
  MODIFY COLUMN `custom_count` int NULL COMMENT '总客户数',
  MODIFY COLUMN `style_code` varchar(255) NULL COMMENT '样式code',
  CHANGE COLUMN type `category_id` bigint NULL COMMENT '门店类型' AFTER `style_code`,
ADD
  COLUMN `category_name` varchar(255) NULL COMMENT '门店类型名称' AFTER `category_id`,
ADD
  COLUMN `style_content` varchar(255) NULL COMMENT '样式内容' AFTER `category_name`,
ADD
  COLUMN `goods_id_list_str` varchar(255) NULL COMMENT '商品id集合' AFTER `style_content`,
ADD
  COLUMN `preview_image` varchar(255) NULL COMMENT '预览图' AFTER `goods_id_list_str`,
ADD
  COLUMN `style_name` varchar(255) NULL COMMENT '样式名称' AFTER `preview_image`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `style_name`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
ADD
  COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_store_name`(`name`) COMMENT '门店名称',
ADD
  INDEX `auto_idx_store_channel_id`(`channel_id`) COMMENT '渠道商ID',
ADD
  INDEX `auto_idx_store_manager_id`(`manager_id`) COMMENT '管理员ID',
ADD
  INDEX `auto_idx_store_model_shop_id`(`model_shop_id`) COMMENT '样板店ID',
ADD
  INDEX `auto_idx_store_style_code`(`style_code`) COMMENT '样式code';
