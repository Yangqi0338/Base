ALTER TABLE
  `goods_zone_goods_rel` DROP COLUMN `is_deleted`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `group_id` bigint NULL COMMENT '分组ID',
  MODIFY COLUMN `spu_id` bigint NULL COMMENT '商品ID',
  MODIFY COLUMN `spu` varchar(255) NULL COMMENT '商品名称快照',
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `spu`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_goods_zone_goods_rel_group_id`(`group_id`) COMMENT '分组ID',
ADD
  INDEX `auto_idx_goods_zone_goods_rel_spu_id`(`spu_id`) COMMENT '商品ID',
  COMMENT = '<p>
商品分组-商品关联表
</p>';
