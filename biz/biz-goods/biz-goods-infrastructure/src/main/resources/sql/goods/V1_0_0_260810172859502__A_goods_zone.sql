ALTER TABLE
  `goods_zone` DROP COLUMN `create_id`,
  DROP COLUMN `create_name`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `group_name` varchar(255) NULL COMMENT '分组名称',
  MODIFY COLUMN `background_img` varchar(255) NULL COMMENT '背景图',
  MODIFY COLUMN `sort_type` int NULL COMMENT '排序类型',
  MODIFY COLUMN `search_box_status` int NULL COMMENT '搜索框显示状态(新增；0 不显示，1 显示；对应产品设计的「搜索框」选项)',
  MODIFY COLUMN `price_show_status` int NULL COMMENT '价格显示状态(新增；0 不显示，1 显示；对应产品设计的「显示价格」勾选)',
  MODIFY COLUMN `store_show_status` int NULL COMMENT '门店显示状态(新增；0 不显示，1 显示；对应产品设计的「显示门店」勾选)',
  MODIFY COLUMN `goods_num` int NULL COMMENT '商品数量',
  MODIFY COLUMN `state` int NULL COMMENT '分组状态(0 禁用，1 启用)',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `state`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
  COMMENT = '<p>
商品分组表（原专区表，按新设计重命名/调整字段）
</p>';
