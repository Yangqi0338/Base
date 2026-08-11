ALTER TABLE
  `store_style` DROP COLUMN `deleted`,
  DROP COLUMN `create_id`,
  DROP COLUMN `create_name`,
  DROP COLUMN `mender_id`,
  DROP COLUMN `mender_name`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `style_code` varchar(255) NULL COMMENT '样式code',
  MODIFY COLUMN `style_name` varchar(255) NULL COMMENT '样式名称',
  MODIFY COLUMN `essential_colour` varchar(255) NULL COMMENT '主色',
  MODIFY COLUMN `auxiliary_color` varchar(255) NULL COMMENT '辅色',
  MODIFY COLUMN `use_store_num` int NULL COMMENT '使用门店数',
  MODIFY COLUMN `type` int NULL COMMENT '类型：1 默认',
  MODIFY COLUMN `state` int NULL COMMENT '状态：0 禁用,1 启用',
  MODIFY COLUMN `source_code` varchar(255) NULL COMMENT '来源模板code' AFTER `state`,
  MODIFY COLUMN `source_name` varchar(255) NULL COMMENT '来源模板名称' AFTER `source_code`,
  MODIFY COLUMN `page_type` varchar(255) NULL COMMENT '页面类型',
  MODIFY COLUMN `goods_id_list_str` varchar(255) NULL COMMENT '商品id集合' AFTER `style_content`,
  MODIFY COLUMN `preview_image` varchar(255) NULL COMMENT '预览图' AFTER `goods_id_list_str`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `preview_image`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_store_style_style_code`(`style_code`) COMMENT '样式code',
ADD
  INDEX `auto_idx_store_style_essential_colour`(`essential_colour`) COMMENT '主色',
ADD
  INDEX `auto_idx_store_style_source_code`(`source_code`) COMMENT '来源模板code',
  COMMENT = '门店样式领域对象';
