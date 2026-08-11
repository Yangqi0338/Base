ALTER TABLE
  `member` DROP COLUMN `nickname`,
  DROP COLUMN `head`,
  DROP COLUMN `count_deal_number`,
  DROP COLUMN `count_deal_amount`,
  DROP COLUMN `merchant_id`,
  DROP COLUMN `residence_province`,
  DROP COLUMN `residence_city`,
  DROP COLUMN `residence_district`,
  DROP COLUMN `id_card`,
  DROP COLUMN `im_sync_status`,
  DROP COLUMN `im_sync_error_msg`,
  DROP COLUMN `account_id`,
  DROP COLUMN `user_account`,
  DROP COLUMN `state`,
  DROP COLUMN `pid`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
ADD
  COLUMN `name` varchar(255) NULL COMMENT '用户名称' AFTER `id`,
  MODIFY COLUMN `background_img` varchar(255) NULL COMMENT '背景图' AFTER `name`,
  MODIFY COLUMN `gender` int NULL COMMENT '性别' AFTER `background_img`,
  MODIFY COLUMN `birthday` date NULL COMMENT '生日' AFTER `gender`,
ADD
  COLUMN `residence` varchar(255) NULL COMMENT '常住地(省份, 城市, 区县)' AFTER `birthday`,
  MODIFY COLUMN `wx_id` varchar(255) NULL COMMENT '微信ID',
  MODIFY COLUMN `open_id` varchar(255) NULL COMMENT 'openId' AFTER `wx_id`,
  MODIFY COLUMN `channel_id` bigint NULL COMMENT '渠道商ID' AFTER `open_id`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `channel_id`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))';
