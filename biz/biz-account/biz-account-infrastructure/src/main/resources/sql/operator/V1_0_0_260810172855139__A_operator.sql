ALTER TABLE
  `operator` DROP COLUMN `name`,
  DROP COLUMN `username`,
  DROP COLUMN `role_id`,
  DROP COLUMN `role_name`,
  DROP COLUMN `dealer_number`,
  DROP COLUMN `one_market_number`,
  DROP COLUMN `two_market_number`,
  DROP COLUMN `invite_channel_number`,
  DROP COLUMN `supplier_goods_count`,
  DROP COLUMN `order_amount`,
  DROP COLUMN `order_total_amount`,
  DROP COLUMN `service_fee_config_v_o`,
  DROP COLUMN `service_amount`,
  DROP COLUMN `proxy_province_code`,
  DROP COLUMN `proxy_city_code`,
  DROP COLUMN `proxy_area_code`,
  DROP COLUMN `proxy_desc`,
  DROP COLUMN `balance_type`,
  DROP COLUMN `info`,
  DROP COLUMN `phone`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `type` int NULL COMMENT '运营类型' AFTER `id`,
  MODIFY COLUMN `type_foreign_id` varchar(255) NULL COMMENT '行业id / 区域地址编码(多选, 拼接)' AFTER `type`,
  MODIFY COLUMN `type_foreign_name` varchar(255) NULL COMMENT '行业名称 / 区域地址' AFTER `type_foreign_id`,
  MODIFY COLUMN `domain` varchar(255) NULL COMMENT '域名' AFTER `type_foreign_name`,
  MODIFY COLUMN `leverage_ratio` int NULL COMMENT '杠杆比例' AFTER `domain`,
ADD
  COLUMN `company_name` varchar(255) NULL COMMENT '公司名称' AFTER `leverage_ratio`,
ADD
  COLUMN `logo` varchar(255) NULL COMMENT '首页logo' AFTER `company_name`,
ADD
  COLUMN `background` varchar(255) NULL COMMENT '首页背景图' AFTER `logo`,
  MODIFY COLUMN `goods_points` int NULL COMMENT '提货积分' AFTER `background`,
  MODIFY COLUMN `level_up_progress` double(6, 2) NULL COMMENT '升级进度' AFTER `goods_points`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `level_up_progress`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))';
